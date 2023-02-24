package com.enwerevincent.investmentwebauthservice.service;

import com.enwerevincent.invest.enums.AppUserStatus;
import com.enwerevincent.invest.enums.RefBonusRewardTime;
import com.enwerevincent.invest.enums.RefRewardMethod;
import com.enwerevincent.invest.model.Settings;
import com.enwerevincent.invest.model.Wallet;
import com.enwerevincent.invest.utils.SiteUtils;
import com.enwerevincent.invest.vo.MailRequest;
import com.enwerevincent.investmentwebauthservice.model.AdminSettings;
import com.enwerevincent.investmentwebauthservice.model.WebUser;
import com.enwerevincent.investmentwebauthservice.model.WebVerificationCode;
import com.enwerevincent.investmentwebauthservice.model.WebWallet;
import com.enwerevincent.investmentwebauthservice.repository.AppUserRepository;
import com.enwerevincent.investmentwebauthservice.repository.AppWalletRepo;
import com.enwerevincent.investmentwebauthservice.repository.SettingsRepo;
import com.enwerevincent.investmentwebauthservice.repository.WebVerificationCodeRepo;
import com.enwerevincent.investmentwebauthservice.request.PasswordResetVo;
import com.enwerevincent.investmentwebauthservice.request.RegisterRequest;
import com.enwerevincent.investmentwebauthservice.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterUserService {

    private final AppUserRepository appUserRepository;

    private final SettingsRepo settingsRepo;
    private final WebVerificationCodeRepo verificationCodeRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final WebVerificationCodeRepo webVerificationCodeRepo;


    private final AppWalletRepo walletRepo;

   // private final EnvAppConfig config;
    @Transactional
    public ApiResponse<String> registerUser(RegisterRequest request,  HttpServletRequest httpServletRequest , String refCode){
        Optional<WebUser> appuser = appUserRepository.findAppUserByEmail(request.getEmail());

        WebUser newUser;
        if (appuser.isEmpty()){
            newUser = new WebUser();
            newUser.setEmail(request.getEmail());
            newUser.setFirstName(request.getFirstName());
            newUser.setLastName(request.getLastName());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setGender(request.getGender());
            newUser.setRefCode(RandomStringUtils.randomAlphanumeric(10));
            newUser.setStatus(AppUserStatus.IN_ACTIVE);
            newUser.setUsername(request.getUsername());
            if(Objects.nonNull(refCode)){
                newUser.setReferrerCode(refCode);
            }
            buildVerificationCode(newUser);
            appUserRepository.save(newUser);
            buildWalletForNewUser(newUser);
          sendWelcomeMail(newUser , httpServletRequest);
        }else{
            return new ApiResponse<>("error" , "User Already Exists");
        }
        rewardReferrer(refCode);

        return new ApiResponse<>("success" , "User Created Successfully");
    }

    @Transactional
    public WebVerificationCode buildVerificationCode(WebUser user){
        WebVerificationCode code = new WebVerificationCode();
        code.setCode(RandomStringUtils.randomAlphanumeric(64));
        code.setCreationTime(LocalDateTime.now());
        code.setUser(user);
        return webVerificationCodeRepo.save(code);
    }

    private void sendWelcomeMail(WebUser user , HttpServletRequest request){
        String code = verificationCodeRepo.findWebVerificationCodeByUser(user)
                .orElseThrow(()-> new RuntimeException("Code Not Found")).getCode();
            log.debug("===**CODE**=== : {}" ,code);
        String verificationUri = buildVerificationUrl(code, request);
        try {
            Map<String , String> templateParams = new HashMap<>();
            templateParams.put("userName" , user.getFirstName());
            templateParams.put("code" , code);
            templateParams.put("url" , verificationUri);
            emailService.sendMail(MailRequest.builder()
                            .subject("Welcome")
                            .from("enwerevincent@gmail.com")
                            .to(user.getEmail())
                            .template("welcome")
                            .templateParams(templateParams)
                            .build());
        }catch (Exception e){
            throw  new IllegalStateException(e);
        }
    }

    private String buildVerificationUrl(String code, HttpServletRequest request){
        String url = SiteUtils.SITE_URL(request);
        return url+"?code="+code;
    }

    public String verifyRegistrationToken(String token){
        Optional<WebVerificationCode> code = verificationCodeRepo.findWebVerificationCodeByCode(token);
        WebVerificationCode verificationCode;
        if (code.isPresent()){
            verificationCode = code.get();
            if (LocalDateTime.now().isBefore(verificationCode.getCreationTime().plusMinutes(5))){
                WebUser user = appUserRepository.findAppUserByEmail(verificationCode.getUser().getEmail()).get();
                user.setStatus(AppUserStatus.ACTIVE);
                appUserRepository.save(user);
                return "Verified";
            }else{
                return "Verification Code Is Expired";
            }
        }
        return "Invalid Verification Code";
    }

    private void rewardReferrer(String refCode){
        if (Objects.nonNull(refCode)){
            Optional<WebUser> user = appUserRepository.findAppUserByRefCode(refCode);
            WebUser referrer;
            if (user.isPresent()){
                referrer = appUserRepository.findAppUserByRefCode(refCode).get();
                Wallet referrerWallet = walletRepo.findWalletByAppUser(referrer);
                referrerWallet.setRefBonusBal(referrerWallet.getRefBonusBal().add(calculateBonus(referrer)));
                List<BigDecimal> amounts = new ArrayList<>(Arrays.asList(referrerWallet.getRefBonusBal() , referrerWallet.getProfitBalance() ));
                referrerWallet.setTotal(getWalletTotal(amounts));
                walletRepo.save(referrerWallet);
            }
        }
    }

    private BigDecimal getWalletTotal(List<BigDecimal> amounts){
        return amounts.stream().reduce(BigDecimal.ZERO , BigDecimal::add);
    }
    private BigDecimal calculateBonus(WebUser user){
        List<AdminSettings> allSettings = settingsRepo.findAll();
        Settings settings = allSettings.get(0);
        BigDecimal bonus = null;
        if (settings.getRefBonusRewardTime().equals(RefBonusRewardTime.ON_REGISTRATION)){
            bonus=  settings.getRefFixedBonus();
        }
        return bonus;
    }

    @Transactional
    public WebWallet buildWalletForNewUser(WebUser user){
        WebWallet webWallet = new WebWallet();
        webWallet.setRefBonusBal(new BigDecimal(0));
        webWallet.setProfitBalance(new BigDecimal(0));
        webWallet.setTotal(new BigDecimal(0));
        webWallet.setAppUser(user);
        return  walletRepo.save(webWallet);
    }

    public String resetPasswordForLoggedInUser(PasswordResetVo resetVo , Long userId ){
        WebUser user = appUserRepository.findById(userId).get();
        log.debug("USER ===...=== {}" , user);
        if (passwordEncoder.matches(resetVo.getOldPassword(),  user.getPassword())){
            user.setPassword(resetVo.getNewPassword());
            appUserRepository.save(user);
            return "Password Reset Done";
        }
        return  "Invalid Old Password";
    }




//    @PostConstruct
//    public AdminSettings mockAdminSettings(){
//       AdminSettings adminSettings = new AdminSettings();
//       adminSettings.setRefFixedBonus(new BigDecimal(20));
//       adminSettings.setRefRewardMethod(RefRewardMethod.FIXED);
//       adminSettings.setRefPercentBonus(10);
//       adminSettings.setRefBonusRewardTime(RefBonusRewardTime.ON_REGISTRATION);
//       return  settingsRepo.save(adminSettings);
//    }

//
//    public class SettingsRequest{
//        private BigDecimal refFixedBonus;
//        private RefRewardMethod refRewardMethod;
//        private RefBonusRewardTime refBonusRewardTime;
//    }

}

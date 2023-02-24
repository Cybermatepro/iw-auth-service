package com.enwerevincent.investmentwebauthservice.rest;

import com.enwerevincent.investmentwebauthservice.model.WebUser;
import com.enwerevincent.investmentwebauthservice.request.PasswordResetVo;
import com.enwerevincent.investmentwebauthservice.request.RegisterRequest;
import com.enwerevincent.investmentwebauthservice.response.ApiResponse;
import com.enwerevincent.investmentwebauthservice.service.RegisterUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class SignUpController {

    private final RegisterUserService registerUserService;

    @PostMapping("/signup")
    public ApiResponse<String> signUp(@RequestBody RegisterRequest request ,
                                      HttpServletRequest httpServletRequest,
                                      @RequestParam(required = false) String refCode
    ){
        log.debug("User : {} Successfully Registered" , request.getEmail());
        return registerUserService.registerUser(request , httpServletRequest , refCode);
    }
    @GetMapping("/verify")
    public ApiResponse<String> verifyToken(@RequestParam String auth){
        return new ApiResponse<>("success" , registerUserService.verifyRegistrationToken(auth));
    }

    @PostMapping("reset-password/{userId}")
    public ApiResponse<String> resetPassword(@RequestBody PasswordResetVo resetVo,
                                             @PathVariable Long userId){
        return new ApiResponse<>("success",registerUserService.resetPasswordForLoggedInUser(resetVo , userId));
    }

}

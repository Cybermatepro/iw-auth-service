package com.enwerevincent.investmentwebauthservice.config.security.service;

import com.enwerevincent.investmentwebauthservice.config.security.web.CustomUserDetails;
import com.enwerevincent.investmentwebauthservice.model.WebUser;
import com.enwerevincent.investmentwebauthservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        WebUser appUser =  appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        if(Objects.nonNull(appUser)){
            log.debug("User : {}", appUser);
            return new CustomUserDetails(appUser);
        }
        log.error("This Username was Not Found");
        return null;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

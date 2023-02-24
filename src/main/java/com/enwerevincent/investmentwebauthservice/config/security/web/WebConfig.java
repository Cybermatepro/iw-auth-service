package com.enwerevincent.investmentwebauthservice.config.security.web;

import com.enwerevincent.investmentwebauthservice.config.EnvAppConfig;
import com.enwerevincent.investmentwebauthservice.config.security.jwt.JwtFilter;
import com.enwerevincent.investmentwebauthservice.config.security.service.CustomUserDetailsService;
import com.enwerevincent.investmentwebauthservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig  {

    private final EnvAppConfig envAppConfig;

    private final JwtFilter jwtRequestFilter;
    private final ResourceLoader resourceLoader;

    private final AppUserRepository appUserRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(   "/.well-known/jwks.json" ,"/api/auth/signup"  ,"/api/auth/authenticate", "/api/auth/verify","/logout" , "role" , "/.well-known/openid-configuration" ).permitAll()
                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout(
                        httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                .logoutRequestMatcher( new AntPathRequestMatcher("/logout"))
                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                );
        // @formatter:on
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
      //  HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory ();
        return new RestTemplate();
    }

}

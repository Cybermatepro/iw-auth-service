package com.enwerevincent.investmentwebauthservice.service;

import com.enwerevincent.investmentwebauthservice.config.security.jwt.JwtUtil;
import com.enwerevincent.investmentwebauthservice.request.AuthRequest;
import com.enwerevincent.investmentwebauthservice.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
     public String authenticateUser(AuthRequest request) throws Exception {
         Authentication authentication;
         try {
             authentication = authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
             );
         } catch (Exception ex) {
             throw new Exception("inavalid username/password");
         }
         SecurityContextHolder.getContext().setAuthentication(authentication);
         return  jwtUtil.generateToken(request.getEmail());
     }
}

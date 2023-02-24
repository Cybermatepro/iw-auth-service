package com.enwerevincent.investmentwebauthservice.rest;

import com.enwerevincent.investmentwebauthservice.config.security.jwt.JwtUtil;
import com.enwerevincent.investmentwebauthservice.request.AuthRequest;
import com.enwerevincent.investmentwebauthservice.response.ApiResponse;
import com.enwerevincent.investmentwebauthservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@RestController
public class AuthController {


    private final AuthService authService;

    @PostMapping("/authenticate")
    public ApiResponse<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        log.info("User Logging in....");
        return new ApiResponse<>("success" , authService.authenticateUser(authRequest));
    }
}

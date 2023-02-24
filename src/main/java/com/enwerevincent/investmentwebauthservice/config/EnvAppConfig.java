package com.enwerevincent.investmentwebauthservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@Service
@ConfigurationProperties("app")
public class EnvAppConfig {

    private Oauth2Config oauth2Config;
    private AppKeyStore appKeyStore;
    private JwtConfig jwtConfig;

    private MailConfig mailConfig;
    @Data
    public static class Oauth2Config{
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String issuer;
    }

    @Data
    public static class AppKeyStore{
        private String path;
        private String password;
        private String pairName;
    }

    @Data
    public static class JwtConfig{
        private String secret;
        private String time;
    }

    @Data
    public static class MailConfig{
        private String from;
    }
}

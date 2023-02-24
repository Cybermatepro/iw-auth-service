package com.enwerevincent.investmentwebauthservice;

import com.enwerevincent.investmentwebauthservice.config.EnvAppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@RefreshScope
@EnableConfigurationProperties(value = {EnvAppConfig.class})
@EnableEurekaClient
public class InvestmentWebAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvestmentWebAuthServiceApplication.class, args);
    }

}

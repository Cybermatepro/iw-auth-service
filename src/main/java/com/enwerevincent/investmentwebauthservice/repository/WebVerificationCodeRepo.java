package com.enwerevincent.investmentwebauthservice.repository;

import com.enwerevincent.investmentwebauthservice.model.WebUser;
import com.enwerevincent.investmentwebauthservice.model.WebVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebVerificationCodeRepo extends JpaRepository<WebVerificationCode, Long> {

    Optional<WebVerificationCode> findWebVerificationCodeByUser(WebUser user);

    Optional<WebVerificationCode> findWebVerificationCodeByCode(String  code);
}

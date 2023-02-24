package com.enwerevincent.investmentwebauthservice.repository;

import com.enwerevincent.investmentwebauthservice.model.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<WebUser, Long> {
    Optional<WebUser> findAppUserByEmail(String email);
    Optional<WebUser> findAppUserByRefCode(String refCode);
}

package com.enwerevincent.investmentwebauthservice.repository;

import com.enwerevincent.investmentwebauthservice.model.WebRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<WebRole, Long> {
    Optional<WebRole> findAppRoleByRole(String role);
}

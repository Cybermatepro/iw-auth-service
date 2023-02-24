package com.enwerevincent.investmentwebauthservice.service;

import com.enwerevincent.investmentwebauthservice.model.WebRole;
import com.enwerevincent.investmentwebauthservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public WebRole createOrGetExistingRole(String role){
        return roleRepository.findAppRoleByRole(role).orElseGet(()->{
            WebRole newAppRole = new WebRole();
                    newAppRole.setRole(role);
            return roleRepository.save(newAppRole);
        });
    }

}

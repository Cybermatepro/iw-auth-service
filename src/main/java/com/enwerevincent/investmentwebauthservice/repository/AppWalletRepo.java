package com.enwerevincent.investmentwebauthservice.repository;

import com.enwerevincent.invest.model.AppUser;
import com.enwerevincent.invest.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppWalletRepo extends JpaRepository<Wallet , Long> {
    Wallet findWalletByAppUser(AppUser appUser);
}

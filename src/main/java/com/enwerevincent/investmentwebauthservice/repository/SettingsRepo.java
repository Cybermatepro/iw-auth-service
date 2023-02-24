package com.enwerevincent.investmentwebauthservice.repository;

import com.enwerevincent.invest.model.AppUser;
import com.enwerevincent.invest.model.Settings;
import com.enwerevincent.investmentwebauthservice.model.AdminSettings;
import com.enwerevincent.investmentwebauthservice.model.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepo  extends JpaRepository<AdminSettings, Long> {

   // Settings findSettingsByUser(WebUser user);
}

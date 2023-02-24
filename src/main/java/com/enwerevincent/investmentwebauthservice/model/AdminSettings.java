package com.enwerevincent.investmentwebauthservice.model;

import com.enwerevincent.invest.model.Settings;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AdminSettings extends Settings {
}

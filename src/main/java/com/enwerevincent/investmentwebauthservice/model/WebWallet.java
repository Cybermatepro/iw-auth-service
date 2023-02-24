package com.enwerevincent.investmentwebauthservice.model;

import com.enwerevincent.invest.model.Wallet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class WebWallet extends Wallet {

}

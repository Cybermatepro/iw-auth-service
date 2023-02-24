package com.enwerevincent.investmentwebauthservice.model;

import com.enwerevincent.invest.model.VerificationCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class WebVerificationCode extends VerificationCode {


    private LocalDateTime creationTime;
}

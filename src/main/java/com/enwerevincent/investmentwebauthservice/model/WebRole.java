package com.enwerevincent.investmentwebauthservice.model;


import com.enwerevincent.invest.model.AppRole;
import lombok.*;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class WebRole extends AppRole {


    public WebRole() {

    }
}

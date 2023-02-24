package com.enwerevincent.investmentwebauthservice.model;


import com.enwerevincent.invest.model.AppUser;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class WebUser extends AppUser {


    public WebUser() {

    }
}

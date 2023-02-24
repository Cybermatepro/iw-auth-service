package com.enwerevincent.investmentwebauthservice.request;

import lombok.Data;

@Data
public class PasswordResetVo {

    private String oldPassword;
    private String newPassword;
}

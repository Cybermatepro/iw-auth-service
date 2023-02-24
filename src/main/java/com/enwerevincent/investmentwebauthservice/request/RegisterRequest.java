package com.enwerevincent.investmentwebauthservice.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String username;
    private String password;
}

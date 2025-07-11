package com.company.projectjwt1.payload;

import lombok.Data;

@Data
public class LoginRequest {

    private String usernameOrEmail;
    private String password;
}

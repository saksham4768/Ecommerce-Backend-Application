package com.ecommerce.project.security.jwt;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String jwtToken;
    private String username;
    private List<String> roles;

    public LoginResponse(String jwtToken, List<String> roles, String username) {
        this.jwtToken = jwtToken;
        this.roles = roles;
        this.username = username;
    }
}

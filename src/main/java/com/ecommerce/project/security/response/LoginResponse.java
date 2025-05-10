package com.ecommerce.project.security.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private Long id;
    private String jwtToken;
    private String username;
    private List<String> roles;

    public LoginResponse(Long id, String jwtToken, List<String> roles, String username) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.roles = roles;
        this.username = username;
    }

    public LoginResponse(Long id, List<String> roles, String username) {
        this.id = id;
        this.roles = roles;
        this.username = username;
    }
}

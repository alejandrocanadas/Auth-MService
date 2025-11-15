package com.example.auth.DTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginResponse {
    String accessToken;
    String role;

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
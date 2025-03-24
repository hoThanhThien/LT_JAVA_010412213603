package com.example.laundry.dto;

public class LoginResponse {
    private String role;
    private String token;

    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

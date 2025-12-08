package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.RoleType;

public class LoginResponse {
    
    private Long id;
    private String token;
    private String username;
    private String fullName;
    private RoleType role;

    public LoginResponse() {
    }

    public LoginResponse(Long id, String token, String username, String fullName, RoleType role) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}

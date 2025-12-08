package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.RoleType;

public class UserAdminRequest {

    private String fullName;
    private String email;
    private String password;     // para crear / resetear
    private RoleType role;       // ADMIN, CAJERO, COCINERO, MESERO
    private boolean active;      // estado del usuario

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

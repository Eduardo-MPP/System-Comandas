package com.restaurant.ordering_system.dto;

import jakarta.validation.constraints.NotBlank;

public class CashRegisterClosingRequest {
    
    @NotBlank(message = "El nombre del cierre es obligatorio")
    private String name;

    // Constructors
    public CashRegisterClosingRequest() {
    }

    public CashRegisterClosingRequest(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

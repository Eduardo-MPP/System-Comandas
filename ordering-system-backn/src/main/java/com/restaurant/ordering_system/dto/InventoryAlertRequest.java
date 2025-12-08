package com.restaurant.ordering_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InventoryAlertRequest {
    
    @NotNull
    private Long inventoryItemId;
    
    @NotNull
    private Integer currentStock;
    
    @NotNull
    private Integer minimumStock;
    
    @NotBlank
    private String severity;

    public InventoryAlertRequest() {
    }

    public InventoryAlertRequest(Long inventoryItemId, Integer currentStock, Integer minimumStock, String severity) {
        this.inventoryItemId = inventoryItemId;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.severity = severity;
    }

    public Long getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(Long inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}

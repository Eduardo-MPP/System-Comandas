package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.TableStatus;

public class TableResponse {
    
    private Long id;
    private String tableNumber;
    private TableStatus status;
    private String zone;
    private Integer capacity;

    public TableResponse() {
    }

    public TableResponse(Long id, String tableNumber, TableStatus status, String zone, Integer capacity) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.status = status;
        this.zone = zone;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}

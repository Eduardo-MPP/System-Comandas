package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.TableStatus;

public class TableAdminDTO {

    private Long id;
    private String tableNumber;   // igual que en la entidad
    private String name;          // usaremos esto para la zona/nombre visible
    private Integer capacity;
    private TableStatus status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
}

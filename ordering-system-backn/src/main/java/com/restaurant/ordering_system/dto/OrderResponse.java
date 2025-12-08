package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.OrderStatus;
import java.time.LocalDateTime;

public class OrderResponse {
    
    private Long id;
    private Long tableId;
    private String tableNumber;
    private String waiterName;
    private OrderStatus status;
    private Integer numberOfGuests;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long tableId, String tableNumber, String waiterName, OrderStatus status, Integer numberOfGuests, LocalDateTime createdAt) {
        this.id = id;
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.waiterName = waiterName;
        this.status = status;
        this.numberOfGuests = numberOfGuests;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

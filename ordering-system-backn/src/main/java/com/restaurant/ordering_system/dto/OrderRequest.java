package com.restaurant.ordering_system.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {
    
    @NotNull
    private Long tableId;
    
    @NotNull
    private Integer numberOfGuests;
    
    private List<OrderItemRequest> items;

    public OrderRequest() {
    }

    public OrderRequest(Long tableId, Integer numberOfGuests, List<OrderItemRequest> items) {
        this.tableId = tableId;
        this.numberOfGuests = numberOfGuests;
        this.items = items;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}

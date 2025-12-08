package com.restaurant.ordering_system.dto;

import java.math.BigDecimal;

public class TopDishDTO {

    private Long menuItemId;
    private String menuItemName;
    private Long timesOrdered;
    private BigDecimal totalAmount;

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public Long getTimesOrdered() {
        return timesOrdered;
    }

    public void setTimesOrdered(Long timesOrdered) {
        this.timesOrdered = timesOrdered;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

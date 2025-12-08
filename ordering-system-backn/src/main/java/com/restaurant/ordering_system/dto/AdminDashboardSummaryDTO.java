package com.restaurant.ordering_system.dto;

import java.math.BigDecimal;

public class AdminDashboardSummaryDTO {

    private long totalTables;
    private long activeTables;           // mesas en uso
    private long totalWaiters;
    private long activeWaiters;          // si luego manejas estado online
    private long totalOrdersToday;
    private BigDecimal totalSalesToday;  // ventas del día (todas las formas de pago)
    private BigDecimal currentShiftIncome; // lo que va sumando la caja en el turno actual

    public AdminDashboardSummaryDTO() {
    }

    public long getTotalTables() {
        return totalTables;
    }

    public void setTotalTables(long totalTables) {
        this.totalTables = totalTables;
    }

    public long getActiveTables() {
        return activeTables;
    }

    public void setActiveTables(long activeTables) {
        this.activeTables = activeTables;
    }

    public long getTotalWaiters() {
        return totalWaiters;
    }

    public void setTotalWaiters(long totalWaiters) {
        this.totalWaiters = totalWaiters;
    }

    public long getActiveWaiters() {
        return activeWaiters;
    }

    public void setActiveWaiters(long activeWaiters) {
        this.activeWaiters = activeWaiters;
    }

    public long getTotalOrdersToday() {
        return totalOrdersToday;
    }

    public void setTotalOrdersToday(long totalOrdersToday) {
        this.totalOrdersToday = totalOrdersToday;
    }

    public BigDecimal getTotalSalesToday() {
        return totalSalesToday;
    }

    public void setTotalSalesToday(BigDecimal totalSalesToday) {
        this.totalSalesToday = totalSalesToday;
    }

    public BigDecimal getCurrentShiftIncome() {
        return currentShiftIncome;
    }

    public void setCurrentShiftIncome(BigDecimal currentShiftIncome) {
        this.currentShiftIncome = currentShiftIncome;
    }
}

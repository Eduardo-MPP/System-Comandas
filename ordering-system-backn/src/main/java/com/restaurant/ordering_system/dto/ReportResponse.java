package com.restaurant.ordering_system.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ReportResponse {
    
    private BigDecimal totalRevenue;
    private Integer totalOrders;
    private Double averageServiceTime;
    private Map<String, BigDecimal> revenueByPaymentMethod;
    private Map<String, Integer> topProducts;
    private Map<String, BigDecimal> salesByCategory;

    public ReportResponse() {
    }

    public ReportResponse(BigDecimal totalRevenue, Integer totalOrders, Double averageServiceTime, Map<String, BigDecimal> revenueByPaymentMethod, Map<String, Integer> topProducts, Map<String, BigDecimal> salesByCategory) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.averageServiceTime = averageServiceTime;
        this.revenueByPaymentMethod = revenueByPaymentMethod;
        this.topProducts = topProducts;
        this.salesByCategory = salesByCategory;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getAverageServiceTime() {
        return averageServiceTime;
    }

    public void setAverageServiceTime(Double averageServiceTime) {
        this.averageServiceTime = averageServiceTime;
    }

    public Map<String, BigDecimal> getRevenueByPaymentMethod() {
        return revenueByPaymentMethod;
    }

    public void setRevenueByPaymentMethod(Map<String, BigDecimal> revenueByPaymentMethod) {
        this.revenueByPaymentMethod = revenueByPaymentMethod;
    }

    public Map<String, Integer> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(Map<String, Integer> topProducts) {
        this.topProducts = topProducts;
    }

    public Map<String, BigDecimal> getSalesByCategory() {
        return salesByCategory;
    }

    public void setSalesByCategory(Map<String, BigDecimal> salesByCategory) {
        this.salesByCategory = salesByCategory;
    }
}

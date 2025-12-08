package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.ReportResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class ReportService {
    
    public ReportResponse generateDailyReport() {
        ReportResponse report = new ReportResponse();
        report.setTotalRevenue(BigDecimal.ZERO);
        report.setTotalOrders(0);
        report.setAverageServiceTime(0.0);
        report.setRevenueByPaymentMethod(new HashMap<>());
        report.setTopProducts(new HashMap<>());
        report.setSalesByCategory(new HashMap<>());
        
        return report;
    }
}

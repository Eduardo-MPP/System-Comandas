package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.ReportResponse;
import com.restaurant.ordering_system.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    
    private final ReportService reportService;
    
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    @GetMapping("/daily")
    public ResponseEntity<ReportResponse> getDailyReport() {
        return ResponseEntity.ok(reportService.generateDailyReport());
    }
}

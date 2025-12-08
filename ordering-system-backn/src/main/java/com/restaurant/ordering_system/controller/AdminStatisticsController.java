package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.*;
import com.restaurant.ordering_system.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
@CrossOrigin(origins = "*")
public class AdminStatisticsController {

    private final StatisticsService statisticsService;

    public AdminStatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/top-dishes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopDishDTO>> getTopDishes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(statisticsService.getTopDishes(start, end));
    }

    @GetMapping("/sales-by-day")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SalesByDayDTO>> getSalesByDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(statisticsService.getSalesByDay(start, end));
    }

    @GetMapping("/payment-methods")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentMethodStatsDTO>> getPaymentMethodStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(statisticsService.getPaymentMethodStats(start, end));
    }

    @GetMapping("/waiters")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WaiterPerformanceDTO>> getWaiterPerformance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(statisticsService.getWaiterPerformance(start, end));
    }
}

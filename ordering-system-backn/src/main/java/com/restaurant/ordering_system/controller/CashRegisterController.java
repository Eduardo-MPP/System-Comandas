package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.*;
import com.restaurant.ordering_system.service.CashRegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cash-register")
@CrossOrigin(origins = "*")
public class CashRegisterController {

    @Autowired
    private CashRegisterService cashRegisterService;

    @GetMapping("/current-summary")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<CashRegisterSummaryDTO> getCurrentSummary() {
        return ResponseEntity.ok(cashRegisterService.getCurrentSummary());
    }

    @PostMapping("/close")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<CashRegisterClosingResponse> closeCashRegister(
            @Valid @RequestBody CashRegisterClosingRequest request) {
        return ResponseEntity.ok(cashRegisterService.closeCashRegister(request));
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<List<CashRegisterClosingResponse>> getHistory() {
        return ResponseEntity.ok(cashRegisterService.getHistory());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<CashRegisterClosingResponse> getClosingDetails(@PathVariable Long id) {
        return ResponseEntity.ok(cashRegisterService.getClosingDetails(id));
    }

    // 🔥 NUEVO ENDPOINT - Obtener transacciones de un cierre específico
    @GetMapping("/{id}/transactions")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<List<PaymentSummaryDTO>> getClosingTransactions(@PathVariable Long id) {
        return ResponseEntity.ok(cashRegisterService.getClosingTransactions(id));
    }
}

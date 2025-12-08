package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.BillResponse;
import com.restaurant.ordering_system.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillController {

    @Autowired
    private BillService billService;

    // Obtener todas las cuentas pendientes
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<List<BillResponse>> getPendingBills() {
        return ResponseEntity.ok(billService.getPendingBills());
    }

    // 🔥 NUEVO: Obtener cuenta por Bill ID (para el modal de pago)
    @GetMapping("/{billId}")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long billId) {
        return ResponseEntity.ok(billService.getBillById(billId));
    }

    // Obtener cuenta por Table ID
    @GetMapping("/table/{tableId}")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<BillResponse> getBillByTableId(@PathVariable Long tableId) {
        return ResponseEntity.ok(billService.getBillByTableId(tableId));
    }

    // Generar cuenta para una mesa
    @PostMapping("/table/{tableId}")
    @PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
    public ResponseEntity<BillResponse> generateBill(@PathVariable Long tableId) {
        return ResponseEntity.ok(billService.generateBill(tableId));
    }
}

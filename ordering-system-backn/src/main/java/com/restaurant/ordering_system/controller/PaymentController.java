package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.BillResponse;
import com.restaurant.ordering_system.dto.PaymentRequest;
import com.restaurant.ordering_system.model.Bill;
import com.restaurant.ordering_system.model.Payment;
import com.restaurant.ordering_system.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Obtener cuentas pendientes (para cajero)
    @GetMapping("/pending")
    public ResponseEntity<List<BillResponse>> getPendingBills() {
        List<Bill> bills = paymentService.getPendingBills();
        List<BillResponse> response = bills.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Procesar pago
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentService.processPayment(request);
        return ResponseEntity.ok(payment);
    }

    private BillResponse convertToResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setTableId(bill.getTable().getId());
        response.setTableNumber(bill.getTable().getTableNumber());
        response.setOrderId(bill.getOrder().getId());
        response.setSubtotal(bill.getSubtotal());
        response.setTax(bill.getTax());
        response.setTotal(bill.getTotal());
        response.setCreatedAt(bill.getCreatedAt());

        // Agregar items
        List<BillResponse.BillItemDTO> items = bill.getOrder().getItems().stream()
                .map(item -> new BillResponse.BillItemDTO(
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .collect(Collectors.toList());
        response.setItems(items);

        return response;
    }
}

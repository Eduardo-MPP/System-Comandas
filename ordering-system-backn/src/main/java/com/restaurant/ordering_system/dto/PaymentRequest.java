package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
    
    @NotNull(message = "El ID de la cuenta es obligatorio")
    private Long billId;
    
    @NotNull(message = "El método de pago es obligatorio")
    private PaymentMethod method;

    // Constructores
    public PaymentRequest() {
    }

    public PaymentRequest(Long billId, PaymentMethod method) {
        this.billId = billId;
        this.method = method;
    }

    // Getters y Setters
    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
}

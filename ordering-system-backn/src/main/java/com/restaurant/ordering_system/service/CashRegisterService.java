package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.*;
import com.restaurant.ordering_system.enums.PaymentMethod;
import com.restaurant.ordering_system.exception.ResourceNotFoundException;
import com.restaurant.ordering_system.model.CashRegisterClosing;
import com.restaurant.ordering_system.model.Payment;
import com.restaurant.ordering_system.model.User;
import com.restaurant.ordering_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashRegisterService {

    @Autowired
    private CashRegisterClosingRepository closingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    public CashRegisterSummaryDTO getCurrentSummary() {
        List<Payment> payments = paymentRepository.findByCashClosingIsNullOrderByPaidAtDesc();

        BigDecimal totalAmount = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal efectivoAmount = payments.stream()
                .filter(p -> p.getMethod() == PaymentMethod.EFECTIVO)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tarjetaAmount = payments.stream()
                .filter(p -> p.getMethod() == PaymentMethod.TARJETA)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal transferenciaAmount = payments.stream()
                .filter(p -> p.getMethod() == PaymentMethod.TRANSFERENCIA)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 🔥 ACTUALIZADO - Agregado waiterName
        List<PaymentSummaryDTO> paymentSummaries = payments.stream()
                .map(p -> new PaymentSummaryDTO(
                        p.getId(),
                        p.getAmount(),
                        p.getMethod(),
                        p.getBill().getTable().getTableNumber(),
                        p.getPaidAt(),
                        p.getBill().getOrder().getWaiter().getFullName() // 🔥 NUEVO
                ))
                .collect(Collectors.toList());

        CashRegisterSummaryDTO summary = new CashRegisterSummaryDTO();
        summary.setTotalAmount(totalAmount);
        summary.setEfectivoAmount(efectivoAmount);
        summary.setTarjetaAmount(tarjetaAmount);
        summary.setTransferenciaAmount(transferenciaAmount);
        summary.setTotalTransactions(payments.size());
        summary.setPayments(paymentSummaries);

        return summary;
    }

    @Transactional
    public CashRegisterClosingResponse closeCashRegister(CashRegisterClosingRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User cashier = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Payment> payments = paymentRepository.findByCashClosingIsNullOrderByPaidAtDesc();

        if (payments.isEmpty()) {
            throw new RuntimeException("No hay transacciones para cerrar");
        }

        BigDecimal totalAmount = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal efectivoAmount = payments.stream()
                .filter(p -> p.getMethod() == PaymentMethod.EFECTIVO)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tarjetaAmount = payments.stream()
                .filter(p -> p.getMethod() == PaymentMethod.TARJETA)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal transferenciaAmount = payments.stream()
                .filter(p -> p.getMethod() == PaymentMethod.TRANSFERENCIA)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CashRegisterClosing closing = new CashRegisterClosing();
        closing.setName(request.getName());
        closing.setCashier(cashier);
        closing.setClosedAt(LocalDateTime.now());
        closing.setTotalAmount(totalAmount);
        closing.setEfectivoAmount(efectivoAmount);
        closing.setTarjetaAmount(tarjetaAmount);
        closing.setTransferenciaAmount(transferenciaAmount);
        closing.setTotalTransactions(payments.size());

        CashRegisterClosing savedClosing = closingRepository.save(closing);

        for (Payment payment : payments) {
            payment.setCashClosing(savedClosing);
            paymentRepository.save(payment);
        }

        return convertToResponse(savedClosing);
    }

    public List<CashRegisterClosingResponse> getHistory() {
        return closingRepository.findAllByOrderByClosedAtDesc().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CashRegisterClosingResponse getClosingDetails(Long id) {
        CashRegisterClosing closing = closingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cierre no encontrado"));
        return convertToResponse(closing);
    }

    // 🔥 NUEVO MÉTODO - Obtener transacciones de un cierre específico
    public List<PaymentSummaryDTO> getClosingTransactions(Long closingId) {
        CashRegisterClosing closing = closingRepository.findById(closingId)
                .orElseThrow(() -> new ResourceNotFoundException("Cierre no encontrado"));

        List<Payment> payments = paymentRepository.findByCashClosingOrderByPaidAtDesc(closing);

        return payments.stream()
                .map(p -> new PaymentSummaryDTO(
                        p.getId(),
                        p.getAmount(),
                        p.getMethod(),
                        p.getBill().getTable().getTableNumber(),
                        p.getPaidAt(),
                        p.getBill().getOrder().getWaiter().getFullName()
                ))
                .collect(Collectors.toList());
    }

    private CashRegisterClosingResponse convertToResponse(CashRegisterClosing closing) {
        CashRegisterClosingResponse response = new CashRegisterClosingResponse();
        response.setId(closing.getId());
        response.setName(closing.getName());
        response.setCashierName(closing.getCashier().getFullName());
        response.setClosedAt(closing.getClosedAt());
        response.setTotalAmount(closing.getTotalAmount());
        response.setEfectivoAmount(closing.getEfectivoAmount());
        response.setTarjetaAmount(closing.getTarjetaAmount());
        response.setTransferenciaAmount(closing.getTransferenciaAmount());
        response.setTotalTransactions(closing.getTotalTransactions());
        return response;
    }
}

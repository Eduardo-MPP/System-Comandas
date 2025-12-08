package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.PaymentRequest;
import com.restaurant.ordering_system.enums.OrderStatus;
import com.restaurant.ordering_system.enums.TableStatus;
import com.restaurant.ordering_system.model.*;
import com.restaurant.ordering_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Bill> getPendingBills() {
        List<Order> ordersWaitingPayment = orderRepository.findByStatus(OrderStatus.ESPERANDO_CUENTA);
        return ordersWaitingPayment.stream()
                .map(order -> billRepository.findByOrderId(order.getId()).orElse(null))
                .filter(bill -> bill != null)
                .toList();
    }

    @Transactional
    public Payment processPayment(PaymentRequest paymentRequest) {
        Bill bill = billRepository.findById(paymentRequest.getBillId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User cashier = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setCashier(cashier);
        payment.setAmount(bill.getTotal());
        payment.setMethod(paymentRequest.getMethod());
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        Order order = bill.getOrder();
        order.setStatus(OrderStatus.PAGADO);
        orderRepository.save(order);

        RestaurantTable table = bill.getTable();
        table.setStatus(TableStatus.LIBRE);
        tableRepository.save(table);

        messagingTemplate.convertAndSend("/topic/tables", table);

        return savedPayment;
    }

    public List<Payment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaidAtBetween(start, end);
    }
}

package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.AdminDashboardSummaryDTO;
import com.restaurant.ordering_system.enums.RoleType;
import com.restaurant.ordering_system.enums.TableStatus;
import com.restaurant.ordering_system.model.Payment;
import com.restaurant.ordering_system.repository.OrderRepository;
import com.restaurant.ordering_system.repository.PaymentRepository;
import com.restaurant.ordering_system.repository.RestaurantTableRepository;
import com.restaurant.ordering_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AdminDashboardService {

    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CashRegisterService cashRegisterService;

    public AdminDashboardService(RestaurantTableRepository tableRepository,
                                 UserRepository userRepository,
                                 OrderRepository orderRepository,
                                 PaymentRepository paymentRepository,
                                 CashRegisterService cashRegisterService) {
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.cashRegisterService = cashRegisterService;
    }

    public AdminDashboardSummaryDTO getSummary() {
        AdminDashboardSummaryDTO dto = new AdminDashboardSummaryDTO();

        long totalTables = tableRepository.count();
        long activeTables = tableRepository.countByStatusIn(
                List.of(
                        TableStatus.OCUPADA,
                        TableStatus.ESPERANDO_PEDIDO,
                        TableStatus.ESPERANDO_CUENTA
                )
        );

        long totalWaiters = userRepository.countByRole(RoleType.MESERO);
        long activeWaiters = totalWaiters;

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        long totalOrdersToday = 0L;

        List<Payment> paymentsToday = paymentRepository.findByPaidAtBetween(startOfDay, endOfDay);
        BigDecimal totalSalesToday = paymentsToday.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var currentSummary = cashRegisterService.getCurrentSummary();
        BigDecimal currentShiftIncome = currentSummary.getTotalAmount();

        dto.setTotalTables(totalTables);
        dto.setActiveTables(activeTables);
        dto.setTotalWaiters(totalWaiters);
        dto.setActiveWaiters(activeWaiters);
        dto.setTotalOrdersToday(totalOrdersToday);
        dto.setTotalSalesToday(totalSalesToday);
        dto.setCurrentShiftIncome(currentShiftIncome);

        return dto;
    }
}

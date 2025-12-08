package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.*;
import com.restaurant.ordering_system.model.OrderItem;
import com.restaurant.ordering_system.model.Payment;
import com.restaurant.ordering_system.repository.OrderItemRepository;
import com.restaurant.ordering_system.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;

    public StatisticsService(OrderItemRepository orderItemRepository,
                            PaymentRepository paymentRepository) {
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<TopDishDTO> getTopDishes(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<OrderItem> items = orderItemRepository.findByOrderCreatedAtBetween(startDateTime, endDateTime);

        Map<Long, TopDishDTO> map = new HashMap<>();

        for (OrderItem item : items) {
            if (item.getMenuItem() == null) {
                continue;
            }
            Long id = item.getMenuItem().getId();
            TopDishDTO dto = map.getOrDefault(id, new TopDishDTO());
            dto.setMenuItemId(id);
            dto.setMenuItemName(item.getMenuItem().getName());

            long currentCount = dto.getTimesOrdered() != null ? dto.getTimesOrdered() : 0L;
            dto.setTimesOrdered(currentCount + item.getQuantity());

            BigDecimal lineTotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal currentTotal = dto.getTotalAmount() != null ? dto.getTotalAmount() : BigDecimal.ZERO;
            dto.setTotalAmount(currentTotal.add(lineTotal));

            map.put(id, dto);
        }

        return map.values().stream()
                .sorted(Comparator.comparing(TopDishDTO::getTimesOrdered).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<SalesByDayDTO> getSalesByDay(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<Payment> payments = paymentRepository.findByPaidAtBetween(startDateTime, endDateTime);

        Map<LocalDate, BigDecimal> dailyTotals = new HashMap<>();

        for (Payment p : payments) {
            LocalDate date = p.getPaidAt().toLocalDate();
            BigDecimal current = dailyTotals.getOrDefault(date, BigDecimal.ZERO);
            dailyTotals.put(date, current.add(p.getAmount()));
        }

        return dailyTotals.entrySet().stream()
                .map(e -> {
                    SalesByDayDTO dto = new SalesByDayDTO();
                    dto.setDate(e.getKey());
                    dto.setTotalAmount(e.getValue());
                    return dto;
                })
                .sorted(Comparator.comparing(SalesByDayDTO::getDate))
                .collect(Collectors.toList());
    }

    public List<PaymentMethodStatsDTO> getPaymentMethodStats(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<Payment> payments = paymentRepository.findByPaidAtBetween(startDateTime, endDateTime);

        Map<String, BigDecimal> totals = new HashMap<>();

        payments.forEach(p -> {
            String method = p.getMethod().name();
            BigDecimal current = totals.getOrDefault(method, BigDecimal.ZERO);
            totals.put(method, current.add(p.getAmount()));
        });

        return totals.entrySet().stream()
                .map(e -> {
                    PaymentMethodStatsDTO dto = new PaymentMethodStatsDTO();
                    dto.setMethod(Enum.valueOf(
                            com.restaurant.ordering_system.enums.PaymentMethod.class,
                            e.getKey()
                    ));
                    dto.setTotalAmount(e.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<WaiterPerformanceDTO> getWaiterPerformance(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<Payment> payments = paymentRepository.findByPaidAtBetween(startDateTime, endDateTime);

        Map<Long, WaiterPerformanceDTO> map = new HashMap<>();

        for (Payment p : payments) {
            if (p.getBill() == null || p.getBill().getOrder() == null
                    || p.getBill().getOrder().getWaiter() == null) {
                continue;
            }

            var waiter = p.getBill().getOrder().getWaiter();
            Long id = waiter.getId();

            WaiterPerformanceDTO dto = map.getOrDefault(id, new WaiterPerformanceDTO());
            dto.setWaiterId(id);
            dto.setWaiterName(waiter.getFullName());

            long currentOrders = dto.getOrdersCount() != null ? dto.getOrdersCount() : 0L;
            dto.setOrdersCount(currentOrders + 1);

            BigDecimal currentTotal = dto.getTotalAmount() != null ? dto.getTotalAmount() : BigDecimal.ZERO;
            dto.setTotalAmount(currentTotal.add(p.getAmount()));

            map.put(id, dto);
        }

        return map.values().stream()
                .sorted(Comparator.comparing(WaiterPerformanceDTO::getTotalAmount).reversed())
                .collect(Collectors.toList());
    }
}

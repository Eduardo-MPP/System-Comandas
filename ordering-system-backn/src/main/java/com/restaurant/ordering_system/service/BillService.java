package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.BillResponse;
import com.restaurant.ordering_system.model.Bill;
import com.restaurant.ordering_system.model.Order;
import com.restaurant.ordering_system.model.OrderItem;
import com.restaurant.ordering_system.model.RestaurantTable;
import com.restaurant.ordering_system.enums.OrderStatus;
import com.restaurant.ordering_system.exception.ResourceNotFoundException;
import com.restaurant.ordering_system.repository.BillRepository;
import com.restaurant.ordering_system.repository.OrderRepository;
import com.restaurant.ordering_system.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {
    
    private final BillRepository billRepository;
    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.18");
    
    @Autowired
    public BillService(BillRepository billRepository, TableRepository tableRepository, OrderRepository orderRepository) {
        this.billRepository = billRepository;
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }
    
    public List<BillResponse> getPendingBills() {
        List<Order> ordersWaitingPayment = orderRepository.findByStatus(OrderStatus.ESPERANDO_CUENTA);
        
        return ordersWaitingPayment.stream()
                .map(order -> {
                    Bill bill = billRepository.findByOrderId(order.getId()).orElse(null);
                    return bill != null ? convertToResponse(bill) : null;
                })
                .filter(billResponse -> billResponse != null)
                .collect(Collectors.toList());
    }
    
    // 🔥 NUEVO: Obtener Bill por ID
    public BillResponse getBillById(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        
        return convertToResponse(bill);
    }
    
    public BillResponse getBillByTableId(Long tableId) {
        Order order = orderRepository.findByTableIdAndStatus(tableId, OrderStatus.ESPERANDO_CUENTA)
                .orElseThrow(() -> new ResourceNotFoundException("No hay cuenta pendiente para esta mesa"));
        
        Bill bill = billRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        
        return convertToResponse(bill);
    }
    
    @Transactional
    public BillResponse generateBill(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));
        
        Order order = orderRepository.findByTableIdAndStatus(tableId, OrderStatus.ESPERANDO_CUENTA)
                .orElseThrow(() -> new ResourceNotFoundException("No hay pedido esperando cuenta en esta mesa"));
        
        Bill existingBill = billRepository.findByOrderId(order.getId()).orElse(null);
        if (existingBill != null) {
            return convertToResponse(existingBill);
        }
        
        BigDecimal subtotal = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        BigDecimal total = subtotal.add(tax);
        
        Bill bill = new Bill();
        bill.setTable(table);
        bill.setOrder(order);
        bill.setSubtotal(subtotal);
        bill.setTax(tax);
        bill.setTotal(total);
        bill.setCreatedAt(LocalDateTime.now());
        
        Bill savedBill = billRepository.save(bill);
        
        return convertToResponse(savedBill);
    }
    
    private BillResponse convertToResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setOrderId(bill.getOrder().getId());
        response.setTableId(bill.getTable().getId());
        response.setTableNumber(bill.getTable().getTableNumber());
        response.setSubtotal(bill.getSubtotal());
        response.setTax(bill.getTax());
        response.setTotal(bill.getTotal());
        response.setCreatedAt(bill.getCreatedAt());
        
        List<BillResponse.BillItemDTO> items = bill.getOrder().getItems().stream()
                .map(this::convertToItemDetail)
                .collect(Collectors.toList());
        response.setItems(items);
        
        return response;
    }
    
    private BillResponse.BillItemDTO convertToItemDetail(OrderItem item) {
        BillResponse.BillItemDTO detail = new BillResponse.BillItemDTO();
        detail.setItemName(item.getMenuItem().getName());
        detail.setQuantity(item.getQuantity());
        detail.setUnitPrice(item.getUnitPrice());
        detail.setSubtotal(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        return detail;
    }
}

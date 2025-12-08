package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.OrderRequest;
import com.restaurant.ordering_system.dto.OrderItemRequest;
import com.restaurant.ordering_system.enums.OrderItemStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        RestaurantTable table = tableRepository.findById(orderRequest.getTableId())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User waiter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Order order = new Order();
        order.setTable(table);
        order.setWaiter(waiter);
        order.setNumberOfGuests(orderRequest.getNumberOfGuests());
        order.setStatus(OrderStatus.PENDIENTE);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Item del menú no encontrado"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setStatus(OrderItemStatus.PENDIENTE);
            orderItem.setNotes(itemRequest.getNotes());

            items.add(orderItemRepository.save(orderItem));
        }

        savedOrder.setItems(items);

        table.setStatus(TableStatus.OCUPADA);
        tableRepository.save(table);

        messagingTemplate.convertAndSend("/topic/kitchen", items);

        return savedOrder;
    }

    @Transactional
    public Order addItemsToOrder(Long orderId, List<OrderItemRequest> itemRequests) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (order.getStatus() == OrderStatus.ESPERANDO_CUENTA || order.getStatus() == OrderStatus.PAGADO) {
            throw new RuntimeException("No se pueden agregar items a un pedido en proceso de pago o pagado");
        }

        if (order.getStatus() == OrderStatus.ENTREGADO) {
            order.setStatus(OrderStatus.PENDIENTE);
        }

        List<OrderItem> nuevosItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : itemRequests) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Item del menú no encontrado"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setStatus(OrderItemStatus.PENDIENTE);
            orderItem.setNotes(itemRequest.getNotes());

            OrderItem itemGuardado = orderItemRepository.save(orderItem);
            order.getItems().add(itemGuardado);
            nuevosItems.add(itemGuardado);
        }

        Order savedOrder = orderRepository.save(order);

        messagingTemplate.convertAndSend("/topic/kitchen", nuevosItems);

        return savedOrder;
    }

    public Order getCurrentOrderByTable(Long tableId) {
        return orderRepository.findByTableIdAndStatusNot(tableId, OrderStatus.PAGADO)
                .orElseThrow(() -> new RuntimeException("No hay pedido activo en esta mesa"));
    }

    public List<Order> getPendingOrders() {
        return orderRepository.findByStatusIn(
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, OrderStatus.LISTO)
        );
    }

    public List<Order> getReadyOrdersByWaiter(Long waiterId) {
        return orderRepository.findByWaiterIdAndStatus(waiterId, OrderStatus.LISTO);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.EN_PREPARACION) {
            order.setStartedAt(LocalDateTime.now());
        } else if (newStatus == OrderStatus.LISTO) {
            order.setCompletedAt(LocalDateTime.now());
            messagingTemplate.convertAndSend("/topic/waiter/" + order.getWaiter().getId(), order);
        } else if (newStatus == OrderStatus.ENTREGADO) {
            if (order.getStartedAt() == null) {
                order.setStartedAt(LocalDateTime.now());
            }
            order.setCompletedAt(LocalDateTime.now());
        } else if (newStatus == OrderStatus.ESPERANDO_CUENTA) {
            // 🔥 CREAR BILL AUTOMÁTICAMENTE
            createBillForOrder(order);
            
            // Cambiar estado de mesa
            RestaurantTable table = order.getTable();
            table.setStatus(TableStatus.ESPERANDO_CUENTA);
            tableRepository.save(table);
        }

        Order savedOrder = orderRepository.save(order);

        messagingTemplate.convertAndSend("/topic/kitchen", savedOrder);

        return savedOrder;
    }

    // 🔥 NUEVO MÉTODO: Crear Bill automáticamente
    @Transactional
    public void createBillForOrder(Order order) {
        // Verificar si ya existe un bill para esta orden
        if (billRepository.findByOrderId(order.getId()).isPresent()) {
            return; // Ya existe, no crear duplicado
        }

        // Calcular totales
        BigDecimal subtotal = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18)); // IGV 18%
        BigDecimal total = subtotal.add(tax);

        // Crear la cuenta (Bill)
        Bill bill = new Bill();
        bill.setTable(order.getTable());
        bill.setOrder(order);
        bill.setSubtotal(subtotal);
        bill.setTax(tax);
        bill.setTotal(total);
        bill.setCreatedAt(LocalDateTime.now());

        billRepository.save(bill);

        // Notificar a caja
        messagingTemplate.convertAndSend("/topic/cashier", bill);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

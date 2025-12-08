package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.OrderRequest;
import com.restaurant.ordering_system.dto.OrderItemRequest;
import com.restaurant.ordering_system.enums.OrderStatus;
import com.restaurant.ordering_system.model.Order;
import com.restaurant.ordering_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    public ResponseEntity<Order> addItemsToOrder(
            @PathVariable Long orderId,
            @RequestBody List<OrderItemRequest> items) {
        Order order = orderService.addItemsToOrder(orderId, items);
        return ResponseEntity.ok(order);
    }

    // 🔥 NUEVO: Enviar cuenta a caja (mesero)
    @PutMapping("/{id}/send-to-billing")
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    public ResponseEntity<Order> sendToBilling(@PathVariable Long id) {
        Order order = orderService.updateOrderStatus(id, OrderStatus.ESPERANDO_CUENTA);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('COCINERO', 'ADMIN')")
    public ResponseEntity<List<Order>> getPendingOrders() {
        List<Order> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/ready/{waiterId}")
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    public ResponseEntity<List<Order>> getReadyOrdersByWaiter(@PathVariable Long waiterId) {
        List<Order> orders = orderService.getReadyOrdersByWaiter(waiterId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COCINERO', 'MESERO', 'ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        Order order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/deliver")
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    public ResponseEntity<Order> markAsDelivered(@PathVariable Long id) {
        Order order = orderService.updateOrderStatus(id, OrderStatus.ENTREGADO);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/table/{tableId}/current")
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    public ResponseEntity<Order> getCurrentOrderByTable(@PathVariable Long tableId) {
        try {
            Order order = orderService.getCurrentOrderByTable(tableId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

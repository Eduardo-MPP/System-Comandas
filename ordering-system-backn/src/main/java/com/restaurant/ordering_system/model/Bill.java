    package com.restaurant.ordering_system.model;

    import jakarta.persistence.*;
    import java.math.BigDecimal;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "bills")
    public class Bill {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @ManyToOne
        @JoinColumn(name = "table_id", nullable = false)
        private RestaurantTable table;
        
        @ManyToOne
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;
        
        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal subtotal;
        
        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal tax;
        
        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal total;
        
        @Column(nullable = false)
        private LocalDateTime createdAt = LocalDateTime.now();

        // Constructores
        public Bill() {
        }

        public Bill(Long id, RestaurantTable table, Order order, BigDecimal subtotal, BigDecimal tax, BigDecimal total, LocalDateTime createdAt) {
            this.id = id;
            this.table = table;
            this.order = order;
            this.subtotal = subtotal;
            this.tax = tax;
            this.total = total;
            this.createdAt = createdAt;
        }

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public RestaurantTable getTable() {
            return table;
        }

        public void setTable(RestaurantTable table) {
            this.table = table;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public void setTax(BigDecimal tax) {
            this.tax = tax;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }

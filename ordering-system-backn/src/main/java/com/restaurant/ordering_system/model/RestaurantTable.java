package com.restaurant.ordering_system.model;

import com.restaurant.ordering_system.enums.TableStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "tables")
public class RestaurantTable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 20)
    private String tableNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TableStatus status = TableStatus.LIBRE;
    
    @Column(length = 50)
    private String zone;
    
    private Integer capacity;

    // Constructores
    public RestaurantTable() {
    }

    public RestaurantTable(Long id, String tableNumber, TableStatus status, String zone, Integer capacity) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.status = status;
        this.zone = zone;
        this.capacity = capacity;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}

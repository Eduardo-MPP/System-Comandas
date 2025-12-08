package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    @Query("SELECT i FROM InventoryItem i WHERE i.currentStock <= i.minimumStock")
    List<InventoryItem> findLowStockItems();
}

package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.model.InventoryItem;
import com.restaurant.ordering_system.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    
    private final InventoryItemRepository inventoryItemRepository;
    
    @Autowired
    public InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }
    
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }
    
    public List<InventoryItem> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems();
    }
}

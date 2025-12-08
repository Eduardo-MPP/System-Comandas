package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.TableResponse;
import com.restaurant.ordering_system.model.RestaurantTable;
import com.restaurant.ordering_system.enums.TableStatus;
import com.restaurant.ordering_system.exception.ResourceNotFoundException;
import com.restaurant.ordering_system.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    
    private final TableRepository tableRepository;
    
    @Autowired
    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }
    
    public List<TableResponse> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<TableResponse> getTablesByStatus(TableStatus status) {
        return tableRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TableResponse updateTableStatus(Long tableId, TableStatus status) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));
        
        table.setStatus(status);
        return convertToResponse(tableRepository.save(table));
    }
    
    private TableResponse convertToResponse(RestaurantTable table) {
        TableResponse response = new TableResponse();
        response.setId(table.getId());
        response.setTableNumber(table.getTableNumber());
        response.setStatus(table.getStatus());
        response.setZone(table.getZone());
        response.setCapacity(table.getCapacity());
        return response;
    }
}

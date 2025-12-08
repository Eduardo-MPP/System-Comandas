package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.TableResponse;
import com.restaurant.ordering_system.enums.TableStatus;
import com.restaurant.ordering_system.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "*")
public class TableController {
    
    private final TableService tableService;
    
    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }
    
    @GetMapping
    public ResponseEntity<List<TableResponse>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TableResponse>> getTablesByStatus(@PathVariable TableStatus status) {
        return ResponseEntity.ok(tableService.getTablesByStatus(status));
    }
    
    @PutMapping("/{tableId}/status")
    public ResponseEntity<TableResponse> updateTableStatus(
            @PathVariable Long tableId,
            @RequestParam TableStatus status) {
        return ResponseEntity.ok(tableService.updateTableStatus(tableId, status));
    }
}

package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.CategoryResponse;
import com.restaurant.ordering_system.dto.MenuItemRequest;
import com.restaurant.ordering_system.dto.MenuItemResponse;
import com.restaurant.ordering_system.service.MenuManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menu")
@CrossOrigin(origins = "*")
public class AdminMenuController {

    private final MenuManagementService menuManagementService;

    public AdminMenuController(MenuManagementService menuManagementService) {
        this.menuManagementService = menuManagementService;
    }

    @GetMapping("/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MenuItemResponse>> getAllMenuItems() {
        return ResponseEntity.ok(menuManagementService.getAllMenuItems());
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(menuManagementService.getAllCategories());
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuManagementService.createMenuItem(request));
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuManagementService.updateMenuItem(id, request));
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuManagementService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}

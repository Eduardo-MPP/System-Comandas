package com.restaurant.ordering_system.controller;

import com.restaurant.ordering_system.dto.TableAdminDTO;
import com.restaurant.ordering_system.service.AdminTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tables")
@CrossOrigin(origins = "*")
public class AdminTableController {

    private final AdminTableService adminTableService;

    public AdminTableController(AdminTableService adminTableService) {
        this.adminTableService = adminTableService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TableAdminDTO>> getAll() {
        return ResponseEntity.ok(adminTableService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TableAdminDTO> create(@RequestBody TableAdminDTO request) {
        return ResponseEntity.ok(adminTableService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TableAdminDTO> update(@PathVariable Long id,
                                                @RequestBody TableAdminDTO request) {
        return ResponseEntity.ok(adminTableService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminTableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.TableAdminDTO;
import com.restaurant.ordering_system.enums.TableStatus;
import com.restaurant.ordering_system.exception.ResourceNotFoundException;
import com.restaurant.ordering_system.model.RestaurantTable;
import com.restaurant.ordering_system.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminTableService {

    private final RestaurantTableRepository tableRepository;

    public AdminTableService(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<TableAdminDTO> getAll() {
        return tableRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableAdminDTO create(TableAdminDTO request) {
        RestaurantTable table = new RestaurantTable();
        apply(request, table);
        RestaurantTable saved = tableRepository.save(table);
        return toDto(saved);
    }

    @Transactional
    public TableAdminDTO update(Long id, TableAdminDTO request) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));
        apply(request, table);
        RestaurantTable updated = tableRepository.save(table);
        return toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mesa no encontrada");
        }
        tableRepository.deleteById(id);
    }

    private void apply(TableAdminDTO dto, RestaurantTable entity) {
        entity.setTableNumber(dto.getTableNumber());                    // String → String
        entity.setZone(dto.getName());                                  // name → zone
        entity.setCapacity(dto.getCapacity());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : TableStatus.LIBRE);
    }

    private TableAdminDTO toDto(RestaurantTable entity) {
        TableAdminDTO dto = new TableAdminDTO();
        dto.setId(entity.getId());
        dto.setTableNumber(entity.getTableNumber());
        dto.setName(entity.getZone());                                  // zone → name
        dto.setCapacity(entity.getCapacity());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

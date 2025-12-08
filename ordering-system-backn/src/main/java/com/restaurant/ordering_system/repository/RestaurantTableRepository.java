package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.RestaurantTable;
import com.restaurant.ordering_system.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    // contar mesas por un solo estado (por si lo necesitas en otros lados)
    long countByStatus(TableStatus status);

    // 🔥 usado en AdminDashboardService: mesas activas (varios estados)
    long countByStatusIn(List<TableStatus> statuses);
}

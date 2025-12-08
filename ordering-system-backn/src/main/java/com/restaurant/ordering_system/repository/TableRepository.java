package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.RestaurantTable;
import com.restaurant.ordering_system.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByStatus(TableStatus status);
    Optional<RestaurantTable> findByTableNumber(String tableNumber);
}

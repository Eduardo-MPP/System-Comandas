package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.InventoryAlert;
import com.restaurant.ordering_system.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryAlertRepository extends JpaRepository<InventoryAlert, Long> {
    List<InventoryAlert> findByStatus(AlertStatus status);
    List<InventoryAlert> findByStatusIn(List<AlertStatus> statuses);
}

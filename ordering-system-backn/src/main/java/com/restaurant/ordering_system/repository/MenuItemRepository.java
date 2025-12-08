package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.MenuItem;
import com.restaurant.ordering_system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // ya lo tienes, se puede seguir usando
    List<MenuItem> findByCategoryId(Long categoryId);

    // ya lo tienes
    List<MenuItem> findByAvailable(Boolean available);

    // útil cuando desde el admin quieras listar por categoría usando la entidad
    List<MenuItem> findByCategory(Category category);

    // útil para mostrar solo los ítems activos de una categoría
    List<MenuItem> findByCategoryAndAvailable(Category category, Boolean available);
}

package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.User;
import com.restaurant.ordering_system.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(RoleType role);

    boolean existsByUsername(String username);

    // 🔥 útil para el dashboard admin (meseros totales)
    long countByRole(RoleType role);

    // 🔥 útil si luego quieres mostrar solo usuarios activos
    long countByRoleAndActiveTrue(RoleType role);
}

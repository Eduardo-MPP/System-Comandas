package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.MenuItemResponse;
import com.restaurant.ordering_system.model.Category;
import com.restaurant.ordering_system.model.MenuItem;
import com.restaurant.ordering_system.exception.ResourceNotFoundException;
import com.restaurant.ordering_system.repository.CategoryRepository;
import com.restaurant.ordering_system.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository,
                        CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponse> getMenuItemsByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItem createMenuItem(MenuItem menuItem) {
        Category category = categoryRepository.findById(menuItem.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        menuItem.setCategory(category);
        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        menuItem.setName(menuItemDetails.getName());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setAvailable(menuItemDetails.getAvailable());
        menuItem.setStock(menuItemDetails.getStock());

        if (menuItemDetails.getCategory() != null) {
            Category category = categoryRepository.findById(menuItemDetails.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            menuItem.setCategory(category);
        }

        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        menuItemRepository.delete(menuItem);
    }

    private MenuItemResponse convertToResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        if (menuItem.getCategory() != null) {
            response.setCategoryId(menuItem.getCategory().getId());
            response.setCategoryName(menuItem.getCategory().getName());
        }
        response.setAvailable(menuItem.getAvailable() != null ? menuItem.getAvailable() : true);
        response.setStock(menuItem.getStock());
        return response;
    }
}

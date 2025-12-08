package com.restaurant.ordering_system.service;

import com.restaurant.ordering_system.dto.CategoryResponse;
import com.restaurant.ordering_system.dto.MenuItemRequest;
import com.restaurant.ordering_system.dto.MenuItemResponse;
import com.restaurant.ordering_system.exception.ResourceNotFoundException;
import com.restaurant.ordering_system.model.Category;
import com.restaurant.ordering_system.model.MenuItem;
import com.restaurant.ordering_system.repository.CategoryRepository;
import com.restaurant.ordering_system.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuManagementService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuManagementService(MenuItemRepository menuItemRepository,
                                CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(category);
        item.setAvailable(request.isAvailable());
        item.setStock(request.getStock());

        MenuItem saved = menuItemRepository.save(item);
        return toResponse(saved);
    }

    @Transactional
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(category);
        item.setAvailable(request.isAvailable());
        item.setStock(request.getStock());

        MenuItem updated = menuItemRepository.save(item);
        return toResponse(updated);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado"));
        item.setAvailable(false);
        menuItemRepository.save(item);
    }

    private MenuItemResponse toResponse(MenuItem item) {
        MenuItemResponse dto = new MenuItemResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        if (item.getCategory() != null) {
            dto.setCategoryId(item.getCategory().getId());
            dto.setCategoryName(item.getCategory().getName());
        }
        dto.setAvailable(item.getAvailable() != null ? item.getAvailable() : true);
        dto.setStock(item.getStock());
        return dto;
    }

    private CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}

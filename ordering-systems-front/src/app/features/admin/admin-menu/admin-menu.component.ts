import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminMenuService, MenuItemResponse, CategoryResponse } from '../../../core/services/admin-menu.service';
import { MenuItemRequest } from '../../../core/models/admin-menu.model';

@Component({
  selector: 'app-admin-menu',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-menu.component.html',
  styleUrl: './admin-menu.component.css'
})
export class AdminMenuComponent implements OnInit {

  menuItems: MenuItemResponse[] = [];
  categories: CategoryResponse[] = [];
  loading = true;
  showModal = false;
  editMode = false;

  currentItem: MenuItemRequest = this.getEmptyItem();
  currentItemId: number | null = null;

  constructor(private adminMenuService: AdminMenuService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.adminMenuService.getAllMenuItems().subscribe({
      next: (items) => {
        this.menuItems = items;
        this.loadCategories();
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  loadCategories(): void {
    this.adminMenuService.getAllCategories().subscribe({
      next: (cats) => {
        this.categories = cats;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  openCreateModal(): void {
    this.editMode = false;
    this.currentItem = this.getEmptyItem();
    this.currentItemId = null;
    this.showModal = true;
  }

  openEditModal(item: MenuItemResponse): void {
    this.editMode = true;
    this.currentItemId = item.id;
    this.currentItem = {
      name: item.name,
      description: item.description,
      price: item.price,
      categoryId: item.categoryId,
      available: item.available,
      stock: item.stock
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  saveItem(): void {
    if (this.editMode && this.currentItemId !== null) {
      this.adminMenuService.updateMenuItem(this.currentItemId, this.currentItem).subscribe({
        next: () => {
          this.closeModal();
          this.loadData();
        }
      });
    } else {
      this.adminMenuService.createMenuItem(this.currentItem).subscribe({
        next: () => {
          this.closeModal();
          this.loadData();
        }
      });
    }
  }

  deleteItem(id: number): void {
    if (confirm('¿Seguro que deseas eliminar este plato?')) {
      this.adminMenuService.deleteMenuItem(id).subscribe({
        next: () => {
          this.loadData();
        }
      });
    }
  }

  private getEmptyItem(): MenuItemRequest {
    return {
      name: '',
      description: '',
      price: 0,
      categoryId: 0,
      available: true,
      stock: null
    };
  }
}

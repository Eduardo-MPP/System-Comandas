import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MenuItemRequest } from '../models/admin-menu.model';
import { environment } from '../../../environments/environment';

// Interfaces locales (puedes moverlas a archivos separados después)
export interface MenuItemResponse {
  id: number;
  name: string;
  description: string;
  price: number;
  categoryId: number;
  categoryName: string;
  available: boolean;
  stock: number | null;
}

export interface CategoryResponse {
  id: number;
  name: string;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class AdminMenuService {

  private apiUrl = `${environment.apiUrl}/admin/menu`;

  constructor(private http: HttpClient) {}

  getAllMenuItems(): Observable<MenuItemResponse[]> {
    return this.http.get<MenuItemResponse[]>(`${this.apiUrl}/items`);
  }

  getAllCategories(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(`${this.apiUrl}/categories`);
  }

  createMenuItem(request: MenuItemRequest): Observable<MenuItemResponse> {
    return this.http.post<MenuItemResponse>(`${this.apiUrl}/items`, request);
  }

  updateMenuItem(id: number, request: MenuItemRequest): Observable<MenuItemResponse> {
    return this.http.put<MenuItemResponse>(`${this.apiUrl}/items/${id}`, request);
  }

  deleteMenuItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/items/${id}`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order, OrderRequest, OrderStatus, OrderItem, OrderItemStatus, OrderItemRequest } from '../models/order.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    private apiUrl = `${environment.apiUrl}/orders`;

    constructor(private http: HttpClient) { }

    createOrder(orderRequest: OrderRequest): Observable<Order> {
        return this.http.post<Order>(this.apiUrl, orderRequest);
    }

    addItemsToOrder(orderId: number, items: OrderItemRequest[]): Observable<Order> {
        return this.http.post<Order>(`${this.apiUrl}/${orderId}/items`, items);
    }

    getCurrentOrderByTable(tableId: number): Observable<Order> {
        return this.http.get<Order>(`${this.apiUrl}/table/${tableId}/current`);
    }

    addItemToOrder(orderId: number, item: OrderItemRequest): Observable<OrderItem> {
        return this.http.post<OrderItem>(`${this.apiUrl}/${orderId}/items`, item);
    }

    updateOrderStatus(orderId: number, status: OrderStatus): Observable<Order> {
        return this.http.put<Order>(`${this.apiUrl}/${orderId}/status?status=${status}`, {});
    }

    updateOrderItemStatus(itemId: number, status: OrderItemStatus): Observable<OrderItem> {
        return this.http.put<OrderItem>(`${this.apiUrl}/items/${itemId}/status?status=${status}`, {});
    }

    getPendingOrders(): Observable<Order[]> {
        return this.http.get<Order[]>(`${this.apiUrl}/pending`);
    }

    getReadyOrdersByWaiter(waiterId: number): Observable<Order[]> {
        return this.http.get<Order[]>(`${this.apiUrl}/ready/${waiterId}`);
    }

    markAsDelivered(orderId: number): Observable<Order> {
        return this.http.put<Order>(`${this.apiUrl}/${orderId}/deliver`, {});
    }

    getOrderById(orderId: number): Observable<Order> {
        return this.http.get<Order>(`${this.apiUrl}/${orderId}`);
    }

    // 🔥 CORREGIDO: Usar el endpoint correcto
    sendToBilling(orderId: number): Observable<Order> {
        return this.http.put<Order>(`${this.apiUrl}/${orderId}/send-to-billing`, {});
    }
}

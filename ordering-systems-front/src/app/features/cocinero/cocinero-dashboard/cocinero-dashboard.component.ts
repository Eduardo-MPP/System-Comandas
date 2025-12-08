import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { OrderService } from '../../../core/services/order.service';
import { WebsocketService } from '../../../core/services/websocket.service';
import { Order, OrderStatus } from '../../../core/models/order.model';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-cocinero-dashboard',
    standalone: true,
    imports: [CommonModule, NavbarComponent],
    templateUrl: './cocinero-dashboard.component.html',
    styleUrl: './cocinero-dashboard.component.css'
})
export class CocineroDashboardComponent implements OnInit, OnDestroy {
    orders: Order[] = [];
    isLoading: boolean = true;
    private wsSubscription?: Subscription;

    constructor(
        private orderService: OrderService,
        private websocketService: WebsocketService
    ) { }

    ngOnInit(): void {
        this.loadOrders();
        this.websocketService.connect();

        this.wsSubscription = this.websocketService.kitchenOrders$.subscribe((data: any) => {
            console.log('Notificación de cocina recibida:', data);
            if (data) {
                this.loadOrders();
            }
        });
    }

    ngOnDestroy(): void {
        if (this.wsSubscription) {
            this.wsSubscription.unsubscribe();
        }
        this.websocketService.disconnect();
    }

    loadOrders(): void {
        this.isLoading = true;
        this.orderService.getPendingOrders().subscribe({
            next: (orders: Order[]) => {
                this.orders = orders;
                this.isLoading = false;
                console.log('Pedidos cargados:', orders);
            },
            error: (error: any) => {
                console.error('Error cargando pedidos:', error);
                this.isLoading = false;
            }
        });
    }

    startPreparation(orderId: number): void {
        this.orderService.updateOrderStatus(orderId, OrderStatus.EN_PREPARACION).subscribe({
            next: () => {
                this.loadOrders();
            },
            error: (error: any) => {
                console.error('Error actualizando estado:', error);
            }
        });
    }

    markAsReady(orderId: number): void {
        this.orderService.updateOrderStatus(orderId, OrderStatus.LISTO).subscribe({
            next: () => {
                this.loadOrders();
                alert('¡Pedido listo! Se notificó al mesero.');
            },
            error: (error: any) => {
                console.error('Error actualizando estado:', error);
            }
        });
    }

    getStatusClass(status: OrderStatus): string {
        switch (status) {
            case OrderStatus.PENDIENTE: return 'status-pendiente';
            case OrderStatus.EN_PREPARACION: return 'status-preparacion';
            case OrderStatus.LISTO: return 'status-listo';
            default: return '';
        }
    }

    getStatusText(status: OrderStatus): string {
        switch (status) {
            case OrderStatus.PENDIENTE: return 'Pendiente';
            case OrderStatus.EN_PREPARACION: return 'En Preparación';
            case OrderStatus.LISTO: return 'Listo';
            default: return status;
        }
    }

    getOrderTotal(order: Order): number {
        if (!order.items) return 0;
        return order.items.reduce((sum, item) => sum + ((item.unitPrice || 0) * item.quantity), 0);
    }
}

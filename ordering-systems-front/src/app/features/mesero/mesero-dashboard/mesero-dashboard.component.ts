import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { TableService } from '../../../core/services/table.service';
import { OrderService } from '../../../core/services/order.service';
import { WebsocketService } from '../../../core/services/websocket.service';
import { RestaurantTable, TableStatus } from '../../../core/models/table.model';
import { Order, OrderStatus } from '../../../core/models/order.model';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-mesero-dashboard',
    standalone: true,
    imports: [CommonModule, NavbarComponent],
    templateUrl: './mesero-dashboard.component.html',
    styleUrl: './mesero-dashboard.component.css'
})
export class MeseroDashboardComponent implements OnInit, OnDestroy {
    tables: RestaurantTable[] = [];
    filteredTables: RestaurantTable[] = [];
    selectedFilter: string = 'TODAS';
    isLoading: boolean = true;
    readyOrders: Order[] = [];
    showToast: boolean = false;
    toastMessage: string = '';
    private wsSubscription?: Subscription;
    private currentUserId?: number;
    
    showBillModal: boolean = false;
    selectedTableForBill?: RestaurantTable;
    billOrder?: Order;
    billSubtotal: number = 0;
    billTotal: number = 0;
    currentUserName: string = '';

    constructor(
        private tableService: TableService,
        private orderService: OrderService,
        private websocketService: WebsocketService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.setupWebSocket();
        this.loadTables();
        this.loadReadyOrders();
        this.loadUserName();
    }

    ngOnDestroy(): void {
        if (this.wsSubscription) {
            this.wsSubscription.unsubscribe();
        }
    }

    loadUserName(): void {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            const user = JSON.parse(userStr);
            this.currentUserName = user.fullName || user.username;
        }
    }

    setupWebSocket(): void {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            const user = JSON.parse(userStr);
            this.currentUserId = user.id;
            this.websocketService.connect(user.id);

            this.wsSubscription = this.websocketService.waiterNotifications$.subscribe((order: any) => {
                if (order) {
                    this.playNotificationSound();
                    this.showNotification(`¡Pedido #${order.id} de ${order.table?.tableNumber} está listo!`);
                    this.loadReadyOrders();
                }
            });
        }
    }

    loadTables(): void {
        this.isLoading = true;
        this.tableService.getAllTables().subscribe({
            next: (tables: RestaurantTable[]) => {
                this.tables = tables;
                this.filterTables(this.selectedFilter);
                this.isLoading = false;
            },
            error: (error: any) => {
                this.isLoading = false;
            }
        });
    }

    loadReadyOrders(): void {
        if (this.currentUserId) {
            this.orderService.getReadyOrdersByWaiter(this.currentUserId).subscribe({
                next: (orders: Order[]) => {
                    this.readyOrders = orders;
                },
                error: () => {}
            });
        }
    }

    markAsDelivered(orderId: number): void {
        this.orderService.markAsDelivered(orderId).subscribe({
            next: () => {
                this.loadReadyOrders();
                this.loadTables();
                this.showNotification('Pedido marcado como entregado');
            },
            error: () => {}
        });
    }

    viewBill(table: RestaurantTable): void {
        this.selectedTableForBill = table;
        
        this.orderService.getCurrentOrderByTable(table.id!).subscribe({
            next: (order: Order) => {
                if (!order || order.status === OrderStatus.PAGADO) {
                    this.showNotification('Esta mesa no tiene pedidos activos para facturar');
                    return;
                }

                if (!order.items || order.items.length === 0) {
                    this.showNotification('No hay items en este pedido');
                    return;
                }
                
                this.billOrder = order;
                this.calculateBill();
                this.showBillModal = true;
            },
            error: (error: any) => {
                console.error('Error cargando cuenta:', error);
                this.showNotification('No hay pedido activo en esta mesa');
            }
        });
    }

    calculateBill(): void {
        if (!this.billOrder?.items) {
            this.billSubtotal = 0;
            this.billTotal = 0;
            return;
        }

        this.billSubtotal = this.billOrder.items.reduce((sum, item) => {
            return sum + ((item.unitPrice || 0) * item.quantity);
        }, 0);

        this.billTotal = this.billSubtotal;
    }

    closeBillModal(): void {
        this.showBillModal = false;
        this.selectedTableForBill = undefined;
        this.billOrder = undefined;
    }

    sendBillToCashier(): void {
        if (!this.billOrder?.id) return;

        this.orderService.sendToBilling(this.billOrder.id).subscribe({
            next: () => {
                this.showNotification('Cuenta enviada a caja correctamente');
                this.closeBillModal();
                this.loadTables();
                this.loadReadyOrders();
            },
            error: () => {
                this.showNotification('Error al enviar la cuenta');
            }
        });
    }

    showNotification(message: string): void {
        this.toastMessage = message;
        this.showToast = true;
        setTimeout(() => {
            this.showToast = false;
        }, 5000);
    }

    playNotificationSound(): void {
        const audio = new Audio('assets/sounds/notification.mp3');
        audio.play().catch(() => {});
    }

    filterTables(status: string): void {
        this.selectedFilter = status;
        if (status === 'TODAS') {
            this.filteredTables = this.tables;
        } else {
            this.filteredTables = this.tables.filter(t => t.status === status);
        }
    }

    getStatusClass(status: TableStatus): string {
        switch (status) {
            case TableStatus.LIBRE: return 'status-libre';
            case TableStatus.OCUPADA: return 'status-ocupada';
            case TableStatus.ESPERANDO_PEDIDO: return 'status-esperando';
            case TableStatus.ESPERANDO_CUENTA: return 'status-cuenta';
            default: return '';
        }
    }

    getStatusText(status: TableStatus): string {
        switch (status) {
            case TableStatus.LIBRE: return 'Libre';
            case TableStatus.OCUPADA: return 'Ocupada';
            case TableStatus.ESPERANDO_PEDIDO: return 'Esperando Pedido';
            case TableStatus.ESPERANDO_CUENTA: return 'Esperando Cuenta';
            default: return status;
        }
    }

    openTable(table: RestaurantTable): void {
        if (table.status === TableStatus.LIBRE || table.status === TableStatus.OCUPADA) {
            this.router.navigate(['/mesero/crear-pedido'], {
                queryParams: { tableId: table.id }
            });
        }
    }

    canOpenTable(status: TableStatus): boolean {
        return status === TableStatus.LIBRE || status === TableStatus.OCUPADA;
    }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { MenuService } from '../../../core/services/menu.service';
import { OrderService } from '../../../core/services/order.service';
import { MenuItem } from '../../../core/models/menu-item.model';
import { Order, OrderStatus, OrderItemRequest } from '../../../core/models/order.model';

interface CartItem extends OrderItemRequest {
    menuItem: MenuItem;
}

@Component({
    selector: 'app-crear-pedido',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent],
    templateUrl: './crear-pedido.component.html',
    styleUrl: './crear-pedido.component.css'
})
export class CrearPedidoComponent implements OnInit {
    tableId: number = 0;
    numberOfGuests: number = 1;
    menuItems: MenuItem[] = [];
    cart: CartItem[] = [];
    currentOrder?: Order;
    isLoading: boolean = true;
    isSubmitting: boolean = false;
    searchTerm: string = '';
    selectedCategory: string = 'TODAS';

    constructor(
        private menuService: MenuService,
        private orderService: OrderService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            this.tableId = +params['tableId'];
            if (!this.tableId) {
                this.router.navigate(['/mesero/dashboard']);
                return;
            }
            this.loadCurrentOrder();
        });
        this.loadMenuItems();
    }

    loadCurrentOrder(): void {
        this.orderService.getCurrentOrderByTable(this.tableId).subscribe({
            next: (order: Order) => {
                this.currentOrder = order;
                this.numberOfGuests = order.numberOfGuests;
            },
            error: (err: any) => {
                if (err.status === 404) {
                    this.currentOrder = undefined;
                } else {
                    console.error('Error inesperado consultando pedido actual:', err);
                    alert('Hubo un problema consultando el pedido actual de la mesa');
                }
            }
        });
    }

    loadMenuItems(): void {
        this.isLoading = true;
        this.menuService.getAllMenuItems().subscribe({
            next: (items: MenuItem[]) => {
                this.menuItems = items.filter(item => item.available);
                this.isLoading = false;
            },
            error: (error: any) => {
                console.error('Error cargando menú:', error);
                this.isLoading = false;
            }
        });
    }

    get filteredMenuItems(): MenuItem[] {
        let filtered = this.menuItems;
        if (this.selectedCategory !== 'TODAS') {
            filtered = filtered.filter(item => item.categoryName === this.selectedCategory);
        }
        if (this.searchTerm) {
            filtered = filtered.filter(item =>
                item.name.toLowerCase().includes(this.searchTerm.toLowerCase())
            );
        }
        return filtered;
    }

    get categories(): string[] {
        const cats = ['TODAS', ...new Set(this.menuItems.map(item => item.categoryName))];
        return cats;
    }

    addToCart(menuItem: MenuItem): void {
        const existingItem = this.cart.find(item => item.menuItemId === menuItem.id);
        if (existingItem) {
            existingItem.quantity++;
        } else {
            this.cart.push({
                menuItemId: menuItem.id,
                quantity: 1,
                notes: '',
                menuItem: menuItem
            });
        }
    }

    removeFromCart(index: number): void {
        this.cart.splice(index, 1);
    }

    updateQuantity(item: CartItem, change: number): void {
        item.quantity += change;
        if (item.quantity <= 0) {
            const index = this.cart.indexOf(item);
            this.removeFromCart(index);
        }
    }

    get cartTotal(): number {
        return this.cart.reduce((sum, item) => sum + (item.menuItem.price * item.quantity), 0);
    }

    get cartItemCount(): number {
        return this.cart.reduce((sum, item) => sum + item.quantity, 0);
    }

    submitOrder(): void {
        if (this.cart.length === 0) {
            alert('Agrega al menos un producto al pedido');
            return;
        }
        this.isSubmitting = true;

        if (this.currentOrder && this.currentOrder.status !== OrderStatus.ESPERANDO_CUENTA && this.currentOrder.status !== OrderStatus.PAGADO) {
            const items: OrderItemRequest[] = this.cart.map(item => ({
                menuItemId: item.menuItemId,
                quantity: item.quantity,
                notes: item.notes
            }));
            this.orderService.addItemsToOrder(this.currentOrder.id!, items).subscribe({
                next: () => {
                    alert('¡Comanda enviada exitosamente!');
                    this.router.navigate(['/mesero/dashboard']);
                },
                error: (error: any) => {
                    console.error('Error agregando items:', error);
                    alert('No se pudieron agregar los nuevos items');
                    this.isSubmitting = false;
                }
            });
        } else {
            const orderRequest = {
                tableId: this.tableId,
                numberOfGuests: this.numberOfGuests,
                items: this.cart.map(item => ({
                    menuItemId: item.menuItemId,
                    quantity: item.quantity,
                    notes: item.notes
                }))
            };
            this.orderService.createOrder(orderRequest).subscribe({
                next: () => {
                    alert('¡Pedido enviado exitosamente!');
                    this.router.navigate(['/mesero/dashboard']);
                },
                error: (error: any) => {
                    console.error('Error creando pedido:', error);
                    alert('Error al crear el pedido');
                    this.isSubmitting = false;
                }
            });
        }
    }

    pedirCuenta(): void {
        if (!this.currentOrder || !this.currentOrder.id) return;
        this.orderService.updateOrderStatus(this.currentOrder.id, OrderStatus.ESPERANDO_CUENTA).subscribe({
            next: () => {
                alert('Solicitud de cuenta enviada a caja');
                this.router.navigate(['/mesero/dashboard']);
            },
            error: () => {
                alert('No se pudo solicitar la cuenta');
            }
        });
    }

    cancel(): void {
        if (confirm('¿Deseas cancelar el pedido?')) {
            this.router.navigate(['/mesero/dashboard']);
        }
    }
}

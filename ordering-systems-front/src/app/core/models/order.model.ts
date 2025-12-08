import { MenuItem } from './menu-item.model';
import { RestaurantTable } from './table.model';

export interface Order {
    id?: number;
    tableId: number;
    table?: RestaurantTable;
    tableNumber?: string;
    waiterName?: string;
    numberOfGuests: number;
    status?: OrderStatus;
    items?: OrderItem[];
    createdAt?: Date;
}

export interface OrderItem {
    id?: number;
    menuItemId: number;
    menuItem?: MenuItem;
    menuItemName?: string;
    quantity: number;
    unitPrice?: number;
    notes?: string;
    status?: OrderItemStatus;
}

export interface OrderRequest {
    tableId: number;
    numberOfGuests: number;
    items: OrderItemRequest[];
}

export interface OrderItemRequest {
    menuItemId: number;
    quantity: number;
    notes?: string;
}

export enum OrderStatus {
    PENDIENTE = 'PENDIENTE',
    EN_PREPARACION = 'EN_PREPARACION',
    LISTO = 'LISTO',
    ENTREGADO = 'ENTREGADO',  // <-- si lo tienes en backend
    ESPERANDO_CUENTA = 'ESPERANDO_CUENTA',
    PAGADO = 'PAGADO'
}

export enum OrderItemStatus {
    PENDIENTE = 'PENDIENTE',
    EN_PREPARACION = 'EN_PREPARACION',
    LISTO = 'LISTO'
}

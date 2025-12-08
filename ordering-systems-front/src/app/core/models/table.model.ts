export interface RestaurantTable {
    id: number;
    tableNumber: string;
    status: TableStatus;
    zone: string;
    capacity: number;
}

export enum TableStatus {
    LIBRE = 'LIBRE',
    OCUPADA = 'OCUPADA',
    ESPERANDO_PEDIDO = 'ESPERANDO_PEDIDO',
    ESPERANDO_CUENTA = 'ESPERANDO_CUENTA'
}

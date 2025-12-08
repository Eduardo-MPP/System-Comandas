export interface Bill {
    id: number; 
    orderId: number;
    tableId: number;
    tableNumber: string;
    subtotal: number;
    tax: number;
    total: number;
    createdAt: Date;
    items: BillItem[];
}

export interface BillItem {
    itemName: string;
    quantity: number;
    unitPrice: number;
    subtotal: number;
}

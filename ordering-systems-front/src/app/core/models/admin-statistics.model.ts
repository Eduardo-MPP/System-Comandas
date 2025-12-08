export interface TopDish {
  menuItemId: number;
  menuItemName: string;
  timesOrdered: number;
  totalAmount: number;
}

export interface SalesByDay {
  date: string;            // ISO string desde backend
  totalAmount: number;
}

export interface PaymentMethodStats {
  method: string;
  totalAmount: number;
}

export interface WaiterPerformance {
  waiterId: number;
  waiterName: string;
  ordersCount: number;
  totalAmount: number;
}

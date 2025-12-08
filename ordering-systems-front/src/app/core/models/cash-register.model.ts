export interface CashRegisterSummary {
    totalAmount: number;
    efectivoAmount: number;
    tarjetaAmount: number;
    transferenciaAmount: number;
    totalTransactions: number;
    payments: PaymentSummary[];
}

export interface PaymentSummary {
    id: number;
    amount: number;
    method: string;
    tableNumber: string;
    paidAt: Date;
    waiterName: string; // 🔥 NUEVO
}

export interface CashRegisterClosingRequest {
    name: string;
}

export interface CashRegisterClosingResponse {
    id: number;
    name: string;
    cashierName: string;
    closedAt: Date;
    totalAmount: number;
    efectivoAmount: number;
    tarjetaAmount: number;
    transferenciaAmount: number;
    totalTransactions: number;
}

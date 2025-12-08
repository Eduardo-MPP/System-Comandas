export interface PaymentRequest {
    billId: number;
    method: PaymentMethod;
}

export enum PaymentMethod {
    EFECTIVO = 'EFECTIVO',
    TARJETA = 'TARJETA',
    TRANSFERENCIA = 'TRANSFERENCIA'
}

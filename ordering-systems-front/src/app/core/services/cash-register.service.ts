import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
    CashRegisterSummary, 
    CashRegisterClosingRequest, 
    CashRegisterClosingResponse,
    PaymentSummary // 🔥 AGREGADO
} from '../models/cash-register.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CashRegisterService {
    private apiUrl = `${environment.apiUrl}/cash-register`;

    constructor(private http: HttpClient) { }

    getCurrentSummary(): Observable<CashRegisterSummary> {
        return this.http.get<CashRegisterSummary>(`${this.apiUrl}/current-summary`);
    }

    closeCashRegister(request: CashRegisterClosingRequest): Observable<CashRegisterClosingResponse> {
        return this.http.post<CashRegisterClosingResponse>(`${this.apiUrl}/close`, request);
    }

    getHistory(): Observable<CashRegisterClosingResponse[]> {
        return this.http.get<CashRegisterClosingResponse[]>(`${this.apiUrl}/history`);
    }

    getClosingDetails(id: number): Observable<CashRegisterClosingResponse> {
        return this.http.get<CashRegisterClosingResponse>(`${this.apiUrl}/${id}`);
    }

    // 🔥 NUEVO - Obtener transacciones de un cierre
    getClosingTransactions(id: number): Observable<PaymentSummary[]> {
        return this.http.get<PaymentSummary[]>(`${this.apiUrl}/${id}/transactions`);
    }
}

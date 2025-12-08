import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bill } from '../models/bill.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class BillService {
    private apiUrl = `${environment.apiUrl}/billing`;

    constructor(private http: HttpClient) { }

    getPendingBills(): Observable<Bill[]> {
        return this.http.get<Bill[]>(`${this.apiUrl}/pending`);
    }

    getBillById(billId: number): Observable<Bill> {
        return this.http.get<Bill>(`${this.apiUrl}/${billId}`);
    }

    generateBill(tableId: number): Observable<Bill> {
        return this.http.post<Bill>(`${this.apiUrl}/table/${tableId}`, {});
    }

    getBillByTableId(tableId: number): Observable<Bill> {
        return this.http.get<Bill>(`${this.apiUrl}/table/${tableId}`);
    }
}

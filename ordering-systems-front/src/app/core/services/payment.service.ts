import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentRequest } from '../models/payment.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    private apiUrl = `${environment.apiUrl}/payments`;

    constructor(private http: HttpClient) { }

    processPayment(paymentRequest: PaymentRequest): Observable<any> {
        return this.http.post(`${this.apiUrl}/process`, paymentRequest);  // 🔥 Agregué /process
    }
}

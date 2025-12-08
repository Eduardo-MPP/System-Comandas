import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TopDish, SalesByDay, PaymentMethodStats, WaiterPerformance } from '../models/admin-statistics.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminStatisticsService {

  private apiUrl = `${environment.apiUrl}/admin/statistics`;

  constructor(private http: HttpClient) {}

  getTopDishes(start: string, end: string): Observable<TopDish[]> {
    const params = new HttpParams().set('start', start).set('end', end);
    return this.http.get<TopDish[]>(`${this.apiUrl}/top-dishes`, { params });
  }

  getSalesByDay(start: string, end: string): Observable<SalesByDay[]> {
    const params = new HttpParams().set('start', start).set('end', end);
    return this.http.get<SalesByDay[]>(`${this.apiUrl}/sales-by-day`, { params });
  }

  getPaymentMethods(start: string, end: string): Observable<PaymentMethodStats[]> {
    const params = new HttpParams().set('start', start).set('end', end);
    return this.http.get<PaymentMethodStats[]>(`${this.apiUrl}/payment-methods`, { params });
  }

  getWaiterPerformance(start: string, end: string): Observable<WaiterPerformance[]> {
    const params = new HttpParams().set('start', start).set('end', end);
    return this.http.get<WaiterPerformance[]>(`${this.apiUrl}/waiters`, { params });
  }
}

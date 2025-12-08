import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RestaurantTable, TableStatus } from '../models/table.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class TableService {
    private apiUrl = `${environment.apiUrl}/tables`;

    constructor(private http: HttpClient) { }

    getAllTables(): Observable<RestaurantTable[]> {
        return this.http.get<RestaurantTable[]>(this.apiUrl);
    }

    getTablesByStatus(status: TableStatus): Observable<RestaurantTable[]> {
        return this.http.get<RestaurantTable[]>(`${this.apiUrl}/status/${status}`);
    }

    updateTableStatus(tableId: number, status: TableStatus): Observable<RestaurantTable> {
        return this.http.put<RestaurantTable>(`${this.apiUrl}/${tableId}/status?status=${status}`, {});
    }
}

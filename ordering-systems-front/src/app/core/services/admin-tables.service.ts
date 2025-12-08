import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TableAdminRequest, TableAdminResponse } from '../models/admin-tables.model';

@Injectable({
  providedIn: 'root'
})
export class AdminTablesService {

  private apiUrl = `${environment.apiUrl}/admin/tables`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<TableAdminResponse[]> {
    return this.http.get<TableAdminResponse[]>(this.apiUrl);
  }

  create(request: TableAdminRequest): Observable<TableAdminResponse> {
    return this.http.post<TableAdminResponse>(this.apiUrl, request);
  }

  update(id: number, request: TableAdminRequest): Observable<TableAdminResponse> {
    return this.http.put<TableAdminResponse>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

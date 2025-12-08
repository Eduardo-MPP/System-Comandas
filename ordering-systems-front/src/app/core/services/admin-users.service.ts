import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserAdminRequest, UserAdminResponse } from '../models/admin-users.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminUsersService {

  private apiUrl = `${environment.apiUrl}/admin/users`;

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserAdminResponse[]> {
    return this.http.get<UserAdminResponse[]>(this.apiUrl);
  }

  createUser(request: UserAdminRequest): Observable<UserAdminResponse> {
    return this.http.post<UserAdminResponse>(this.apiUrl, request);
  }

  updateUser(id: number, request: UserAdminRequest): Observable<UserAdminResponse> {
    return this.http.put<UserAdminResponse>(`${this.apiUrl}/${id}`, request);
  }

  deactivateUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  resetPassword(id: number, newPassword: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/reset-password`, newPassword);
  }
}

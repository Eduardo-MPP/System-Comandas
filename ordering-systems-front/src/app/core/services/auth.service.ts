import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';
import {
    LoginRequest,
    LoginResponse,
    User,
    RoleType,
} from '../models/user.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private apiUrl = `${environment.apiUrl}/auth`;
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private http: HttpClient, private router: Router) {
        this.loadUserFromStorage();
    }

    login(credentials: LoginRequest): Observable<LoginResponse> {
        return this.http
            .post<LoginResponse>(`${this.apiUrl}/login`, credentials)
            .pipe(
                tap((response) => {
                    localStorage.setItem('token', response.token);
                    localStorage.setItem(
                        'user',
                        JSON.stringify({
                            id: response.id,
                            username: response.username,
                            fullName: response.fullName,
                            role: response.role,
                        })
                    );
                    this.currentUserSubject.next({
                        id: response.id,
                        username: response.username,
                        fullName: response.fullName,
                        role: response.role,
                    });
                })
            );
    }

    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    getCurrentUser(): User | null {
        return this.currentUserSubject.value;
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }

    hasRole(role: RoleType): boolean {
        const user = this.getCurrentUser();
        return user?.role === role;
    }

    private loadUserFromStorage(): void {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            this.currentUserSubject.next(JSON.parse(userStr));
        }
    }
}

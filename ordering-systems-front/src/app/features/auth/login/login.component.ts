import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest, RoleType } from '../../../core/models/user.model';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
    credentials: LoginRequest = {
        username: '',
        password: ''
    };
    errorMessage: string = '';
    isLoading: boolean = false;

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    onSubmit(): void {
        if (!this.credentials.username || !this.credentials.password) {
            this.errorMessage = 'Por favor complete todos los campos';
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';

        this.authService.login(this.credentials).subscribe({
            next: (response) => {
                this.isLoading = false;
                this.redirectByRole(response.role);
            },
            error: (error) => {
                this.isLoading = false;
                this.errorMessage = 'Usuario o contraseña incorrectos';
                console.error('Error en login:', error);
            }
        });
    }

    private redirectByRole(role: RoleType): void {
        switch (role) {
            case RoleType.MESERO:
                this.router.navigate(['/mesero/dashboard']);
                break;
            case RoleType.COCINERO:
                this.router.navigate(['/cocinero/dashboard']);
                break;
            case RoleType.CAJERO:
                this.router.navigate(['/cajero/dashboard']);
                break;
            case RoleType.ADMIN:
                this.router.navigate(['/admin/dashboard']);
                break;
            default:
                this.router.navigate(['/login']);
        }
    }
}

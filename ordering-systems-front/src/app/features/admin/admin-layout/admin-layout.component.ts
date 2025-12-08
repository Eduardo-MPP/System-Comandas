import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../core/models/user.model';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, NavbarComponent],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent {

  adminName: string | null = null;
  adminEmail: string | null = null;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    const user: User | null = this.authService.getCurrentUser() as User | null;
    this.adminName = user?.fullName || 'Administrador';
    this.adminEmail = user?.username || '';
  }

  navigate(path: string): void {
    this.router.navigate(['/admin', path]);
  }

  isActive(path: string): boolean {
    return this.router.url.startsWith(`/admin/${path}`);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

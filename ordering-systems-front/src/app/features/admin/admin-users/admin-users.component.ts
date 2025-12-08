import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminUsersService } from '../../../core/services/admin-users.service';
import { UserAdminRequest, UserAdminResponse } from '../../../core/models/admin-users.model';
import { RoleType } from '../../../core/models/user.model';

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-users.component.html',
  styleUrl: './admin-users.component.css'
})
export class AdminUsersComponent implements OnInit {

  users: UserAdminResponse[] = [];
  loading = true;
  showModal = false;
  showPasswordModal = false;
  editMode = false;

  currentUser: UserAdminRequest = this.getEmptyUser();
  currentUserId: number | null = null;
  newPassword = '';

  roles = Object.values(RoleType);

  constructor(private adminUsersService: AdminUsersService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.adminUsersService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  openCreateModal(): void {
    this.editMode = false;
    this.currentUser = this.getEmptyUser();
    this.currentUserId = null;
    this.showModal = true;
  }

  openEditModal(user: UserAdminResponse): void {
    this.editMode = true;
    this.currentUserId = user.id;
    this.currentUser = {
      fullName: user.fullName,
      email: user.email,
      password: '',
      role: user.role,
      active: user.active
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  saveUser(): void {
    if (this.editMode && this.currentUserId !== null) {
      this.adminUsersService.updateUser(this.currentUserId, this.currentUser).subscribe({
        next: () => {
          this.closeModal();
          this.loadUsers();
        }
      });
    } else {
      this.adminUsersService.createUser(this.currentUser).subscribe({
        next: () => {
          this.closeModal();
          this.loadUsers();
        }
      });
    }
  }

  deactivateUser(id: number): void {
    if (confirm('¿Seguro que deseas desactivar este usuario?')) {
      this.adminUsersService.deactivateUser(id).subscribe({
        next: () => {
          this.loadUsers();
        }
      });
    }
  }

  openPasswordModal(userId: number): void {
    this.currentUserId = userId;
    this.newPassword = '';
    this.showPasswordModal = true;
  }

  closePasswordModal(): void {
    this.showPasswordModal = false;
  }

  resetPassword(): void {
    if (this.currentUserId !== null && this.newPassword.trim()) {
      this.adminUsersService.resetPassword(this.currentUserId, this.newPassword).subscribe({
        next: () => {
          this.closePasswordModal();
          alert('Contraseña actualizada correctamente');
        }
      });
    }
  }

  getRoleLabel(role: RoleType): string {
    const labels: Record<RoleType, string> = {
      [RoleType.ADMIN]: 'Administrador',
      [RoleType.MESERO]: 'Mesero',
      [RoleType.COCINERO]: 'Cocinero',
      [RoleType.CAJERO]: 'Cajero'
    };
    return labels[role];
  }

  private getEmptyUser(): UserAdminRequest {
    return {
      fullName: '',
      email: '',
      password: '',
      role: RoleType.MESERO,
      active: true
    };
  }
}

import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { OrderService } from '../../../core/services/order.service';
import { User } from '../../../core/models/user.model';
import { Order } from '../../../core/models/order.model';

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './navbar.component.html',
    styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy {
    currentUser: User | null = null;
    showUserMenu: boolean = false;
    readyOrdersCount: number = 0;
    private pollingInterval?: any;

    @Input() readyOrdersCount$?: number;

    constructor(
        private authService: AuthService,
        private orderService: OrderService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.authService.currentUser$.subscribe((user: User | null) => {
            this.currentUser = user;
            if (user && user.role === 'MESERO') {
                this.loadReadyOrdersCount();
                this.startPolling();
            }
        });
    }

    ngOnDestroy(): void {
        if (this.pollingInterval) {
            clearInterval(this.pollingInterval);
        }
    }

    loadReadyOrdersCount(): void {
        if (this.currentUser?.id) {
            this.orderService.getReadyOrdersByWaiter(this.currentUser.id).subscribe({
                next: (orders: Order[]) => {
                    this.readyOrdersCount = orders.length;
                },
                error: (error: any) => {
                    console.error('Error cargando contador:', error);
                }
            });
        }
    }

    startPolling(): void {
        this.pollingInterval = setInterval(() => {
            this.loadReadyOrdersCount();
        }, 10000);
    }

    toggleUserMenu(): void {
        this.showUserMenu = !this.showUserMenu;
    }

    logout(): void {
        this.authService.logout();
    }

    getRoleName(): string {
        switch (this.currentUser?.role) {
            case 'MESERO': return 'Mesero';
            case 'COCINERO': return 'Cocinero';
            case 'CAJERO': return 'Cajero';
            case 'ADMIN': return 'Administrador';
            default: return '';
        }
    }

    isMesero(): boolean {
        return this.currentUser?.role === 'MESERO';
    }
}

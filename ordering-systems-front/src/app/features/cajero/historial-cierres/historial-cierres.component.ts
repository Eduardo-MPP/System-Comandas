import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { CashRegisterService } from '../../../core/services/cash-register.service';
import { CashRegisterClosingResponse } from '../../../core/models/cash-register.model';

@Component({
    selector: 'app-historial-cierres',
    standalone: true,
    imports: [CommonModule, NavbarComponent],
    templateUrl: './historial-cierres.component.html',
    styleUrl: './historial-cierres.component.css'
})
export class HistorialCierresComponent implements OnInit {
    closings: CashRegisterClosingResponse[] = [];
    isLoading: boolean = true;

    constructor(
        private cashRegisterService: CashRegisterService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadHistory();
    }

    loadHistory(): void {
        this.isLoading = true;
        this.cashRegisterService.getHistory().subscribe({
            next: (closings: CashRegisterClosingResponse[]) => {
                this.closings = closings;
                this.isLoading = false;
            },
            error: (error: any) => {
                console.error('Error cargando historial:', error);
                this.isLoading = false;
            }
        });
    }

    goBack(): void {
        this.router.navigate(['/cajero/dashboard']);
    }

    viewDetails(closingId: number): void {
        this.router.navigate(['/cajero/detalle-cierre', closingId]);
    }

    formatDate(date: Date): string {
        return new Date(date).toLocaleString('es-PE', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    getMethodColor(method: string): string {
        switch (method) {
            case 'EFECTIVO': return '#fbbf24';
            case 'TARJETA': return '#3b82f6';
            case 'TRANSFERENCIA': return '#8b5cf6';
            default: return '#10b981';
        }
    }
}

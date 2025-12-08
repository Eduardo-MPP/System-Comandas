import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { CashRegisterService } from '../../../core/services/cash-register.service';
import { CashRegisterSummary, PaymentSummary } from '../../../core/models/cash-register.model';

@Component({
    selector: 'app-transacciones-turno',
    standalone: true,
    imports: [CommonModule, NavbarComponent],
    templateUrl: './transacciones-turno.component.html',
    styleUrl: './transacciones-turno.component.css'
})
export class TransaccionesTurnoComponent implements OnInit {
    cashSummary: CashRegisterSummary | null = null;
    transactions: PaymentSummary[] = [];
    isLoading: boolean = true;

    constructor(
        private cashRegisterService: CashRegisterService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadTransactions();
    }

    loadTransactions(): void {
        this.isLoading = true;
        this.cashRegisterService.getCurrentSummary().subscribe({
            next: (summary: CashRegisterSummary) => {
                this.cashSummary = summary;
                this.transactions = summary.payments;
                this.isLoading = false;
            },
            error: (error: any) => {
                console.error('Error cargando transacciones:', error);
                this.isLoading = false;
            }
        });
    }

    goBack(): void {
        this.router.navigate(['/cajero/dashboard']);
    }

    formatTime(date: Date): string {
        return new Date(date).toLocaleTimeString('es-PE', {
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    getMethodIcon(method: string): string {
        switch (method) {
            case 'EFECTIVO': return '💵';
            case 'TARJETA': return '💳';
            case 'TRANSFERENCIA': return '🏦';
            default: return '💰';
        }
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

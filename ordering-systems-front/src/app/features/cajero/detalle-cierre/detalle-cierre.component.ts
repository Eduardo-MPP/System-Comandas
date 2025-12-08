import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { CashRegisterService } from '../../../core/services/cash-register.service';
import { CashRegisterClosingResponse, PaymentSummary } from '../../../core/models/cash-register.model';

@Component({
    selector: 'app-detalle-cierre',
    standalone: true,
    imports: [CommonModule, NavbarComponent],
    templateUrl: './detalle-cierre.component.html',
    styleUrl: './detalle-cierre.component.css'
})
export class DetalleCierreComponent implements OnInit {
    closing: CashRegisterClosingResponse | null = null;
    transactions: PaymentSummary[] = [];
    isLoading: boolean = true;
    closingId: number = 0;

    constructor(
        private cashRegisterService: CashRegisterService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.closingId = Number(this.route.snapshot.paramMap.get('id'));
        this.loadClosingDetails();
        this.loadTransactions();
    }

    loadClosingDetails(): void {
        this.cashRegisterService.getClosingDetails(this.closingId).subscribe({
            next: (closing: CashRegisterClosingResponse) => {
                this.closing = closing;
            },
            error: (error: any) => {
                console.error('Error cargando detalle del cierre:', error);
            }
        });
    }

    loadTransactions(): void {
        this.isLoading = true;
        this.cashRegisterService.getClosingTransactions(this.closingId).subscribe({
            next: (transactions: PaymentSummary[]) => {
                this.transactions = transactions;
                this.isLoading = false;
            },
            error: (error: any) => {
                console.error('Error cargando transacciones:', error);
                this.isLoading = false;
            }
        });
    }

    goBack(): void {
        this.router.navigate(['/cajero/historial-cierres']);
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
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { ClosingModalComponent } from '../closing-modal/closing-modal.component'; // 🔥 AGREGADO
import { BillService } from '../../../core/services/bill.service';
import { CashRegisterService } from '../../../core/services/cash-register.service';
import { Bill } from '../../../core/models/bill.model';
import { CashRegisterSummary } from '../../../core/models/cash-register.model';

@Component({
    selector: 'app-cajero-dashboard',
    standalone: true,
    imports: [CommonModule, NavbarComponent, ClosingModalComponent], // 🔥 AGREGADO ClosingModalComponent
    templateUrl: './cajero-dashboard.component.html',
    styleUrl: './cajero-dashboard.component.css'
})
export class CajeroDashboardComponent implements OnInit {
    pendingBills: Bill[] = [];
    cashSummary: CashRegisterSummary | null = null;
    isLoading: boolean = true;
    showClosingModal: boolean = false;

    constructor(
        private billService: BillService,
        private cashRegisterService: CashRegisterService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadPendingBills();
        this.loadCashSummary();
    }

    loadPendingBills(): void {
        this.isLoading = true;
        this.billService.getPendingBills().subscribe({
            next: (bills: Bill[]) => {
                this.pendingBills = bills;
                this.isLoading = false;
            },
            error: (error: any) => {
                console.error('Error cargando cuentas:', error);
                this.isLoading = false;
            }
        });
    }

    loadCashSummary(): void {
        this.cashRegisterService.getCurrentSummary().subscribe({
            next: (summary: CashRegisterSummary) => {
                this.cashSummary = summary;
            },
            error: (error: any) => {
                console.error('Error cargando resumen de caja:', error);
            }
        });
    }

    generateBill(bill: Bill, event?: Event): void {
        if (event) {
            event.preventDefault();
            event.stopPropagation();
        }
        
        this.router.navigate(['/cajero/generar-factura'], {
            queryParams: { billId: bill.id }
        });
    }

    openClosingModal(): void {
        this.showClosingModal = true;
    }

    closeClosingModal(): void {
        this.showClosingModal = false;
    }

    onCashClosed(): void {
        this.showClosingModal = false;
        this.loadCashSummary();
        alert('Caja cerrada exitosamente');
    }

    viewHistory(): void {
        this.router.navigate(['/cajero/historial-cierres']);
    }

    viewTransactions(): void {
        this.router.navigate(['/cajero/transacciones-turno']);
    }
}

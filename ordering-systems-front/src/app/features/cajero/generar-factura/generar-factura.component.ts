import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { BillService } from '../../../core/services/bill.service';
import { PaymentService } from '../../../core/services/payment.service';
import { Bill } from '../../../core/models/bill.model';
import { PaymentMethod } from '../../../core/models/payment.model';

@Component({
    selector: 'app-generar-factura',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent],
    templateUrl: './generar-factura.component.html',
    styleUrl: './generar-factura.component.css'
})
export class GenerarFacturaComponent implements OnInit {
    billId: number = 0;
    bill: Bill | null = null;
    selectedPaymentMethod: PaymentMethod = PaymentMethod.EFECTIVO;
    isLoading: boolean = true;
    isProcessing: boolean = false;
    PaymentMethod = PaymentMethod;

    constructor(
        private billService: BillService,
        private paymentService: PaymentService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            this.billId = +params['billId'];
            if (!this.billId) {
                this.router.navigate(['/cajero/dashboard']);
            } else {
                this.loadBillDetails();
            }
        });
    }

    loadBillDetails(): void {
        this.isLoading = true;
        this.billService.getBillById(this.billId).subscribe({
            next: (bill: Bill) => {
                this.bill = bill;
                this.isLoading = false;
            },
            error: (error: any) => {
                console.error('Error cargando detalles de cuenta:', error);
                alert('Error al cargar los detalles de la cuenta');
                this.router.navigate(['/cajero/dashboard']);
            }
        });
    }

    processPayment(): void {
        if (!this.bill) return;

        this.isProcessing = true;

        const paymentRequest = {
            billId: this.bill.id,  // 🔥 Cambié de billId a id
            method: this.selectedPaymentMethod
        };

        this.paymentService.processPayment(paymentRequest).subscribe({
            next: () => {
                alert('¡Pago procesado exitosamente!');
                this.router.navigate(['/cajero/dashboard']);
            },
            error: (error: any) => {
                console.error('Error procesando pago:', error);
                alert('Error al procesar el pago');
                this.isProcessing = false;
            }
        });
    }

    cancel(): void {
        if (confirm('¿Deseas cancelar y volver?')) {
            this.router.navigate(['/cajero/dashboard']);
        }
    }

    printBill(): void {
        window.print();
    }
}

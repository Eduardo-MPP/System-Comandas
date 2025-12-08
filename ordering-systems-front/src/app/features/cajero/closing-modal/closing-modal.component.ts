import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CashRegisterService } from '../../../core/services/cash-register.service';
import { CashRegisterSummary } from '../../../core/models/cash-register.model';

@Component({
    selector: 'app-closing-modal',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './closing-modal.component.html',
    styleUrl: './closing-modal.component.css'
})
export class ClosingModalComponent {
    @Input() cashSummary: CashRegisterSummary | null = null;
    @Output() close = new EventEmitter<void>();
    @Output() cashClosed = new EventEmitter<void>();

    closingName: string = '';
    isProcessing: boolean = false;

    constructor(private cashRegisterService: CashRegisterService) { }

    onClose(): void {
        if (!this.isProcessing) {
            this.close.emit();
        }
    }

    onConfirm(): void {
        if (!this.closingName.trim()) {
            alert('Por favor ingresa un nombre para el cierre');
            return;
        }

        this.isProcessing = true;

        this.cashRegisterService.closeCashRegister({ name: this.closingName }).subscribe({
            next: () => {
                this.isProcessing = false;
                this.cashClosed.emit();
            },
            error: (error: any) => {
                console.error('Error cerrando caja:', error);
                alert('Error al cerrar la caja');
                this.isProcessing = false;
            }
        });
    }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminStatisticsService } from '../../../core/services/admin-statistics.service';
import { TopDish, SalesByDay, PaymentMethodStats, WaiterPerformance } from '../../../core/models/admin-statistics.model';

@Component({
  selector: 'app-admin-statistics',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-statistics.component.html',
  styleUrl: './admin-statistics.component.css'
})
export class AdminStatisticsComponent implements OnInit {

  startDate: string;
  endDate: string;

  topDishes: TopDish[] = [];
  salesByDay: SalesByDay[] = [];
  paymentMethods: PaymentMethodStats[] = [];
  waiterPerformance: WaiterPerformance[] = [];

  loading = false;

  constructor(private statisticsService: AdminStatisticsService) {
    const today = new Date();
    const sevenDaysAgo = new Date();
    sevenDaysAgo.setDate(today.getDate() - 7);

    this.endDate = today.toISOString().substring(0, 10);
    this.startDate = sevenDaysAgo.toISOString().substring(0, 10);
  }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    const start = this.startDate;
    const end = this.endDate;

    this.statisticsService.getTopDishes(start, end).subscribe({
      next: (data) => this.topDishes = data,
      error: () => {}
    });

    this.statisticsService.getSalesByDay(start, end).subscribe({
      next: (data) => this.salesByDay = data,
      error: () => {}
    });

    this.statisticsService.getPaymentMethods(start, end).subscribe({
      next: (data) => this.paymentMethods = data,
      error: () => {}
    });

    this.statisticsService.getWaiterPerformance(start, end).subscribe({
      next: (data) => {
        this.waiterPerformance = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    this.loadAll();
  }

  formatDate(dateStr: string): string {
    const d = new Date(dateStr);
    return d.toLocaleDateString();
  }
}

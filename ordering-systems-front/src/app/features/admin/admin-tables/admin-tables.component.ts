import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminTablesService } from '../../../core/services/admin-tables.service';
import { TableAdminRequest, TableAdminResponse } from '../../../core/models/admin-tables.model';
import { TableStatus } from '../../../core/models/table-status.enum';

@Component({
  selector: 'app-admin-tables',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-tables.component.html',
  styleUrl: './admin-tables.component.css'
})
export class AdminTablesComponent implements OnInit {

  tables: TableAdminResponse[] = [];
  loading = true;
  showModal = false;
  editMode = false;

  currentTable: TableAdminRequest = this.emptyTable();
  currentId: number | null = null;

  statuses = Object.values(TableStatus);

  constructor(private adminTablesService: AdminTablesService) {}

  ngOnInit(): void {
    this.loadTables();
  }

  loadTables(): void {
    this.loading = true;
    this.adminTablesService.getAll().subscribe({
      next: (res) => {
        this.tables = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  openCreate(): void {
    this.editMode = false;
    this.currentTable = this.emptyTable();
    this.currentId = null;
    this.showModal = true;
  }

  openEdit(table: TableAdminResponse): void {
    this.editMode = true;
    this.currentId = table.id;
    this.currentTable = {
      tableNumber: table.tableNumber,
      name: table.name,
      capacity: table.capacity,
      status: table.status
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  save(): void {
    if (this.editMode && this.currentId !== null) {
      this.adminTablesService.update(this.currentId, this.currentTable).subscribe({
        next: () => {
          this.closeModal();
          this.loadTables();
        }
      });
    } else {
      this.adminTablesService.create(this.currentTable).subscribe({
        next: () => {
          this.closeModal();
          this.loadTables();
        }
      });
    }
  }

  delete(id: number): void {
    if (confirm('¿Seguro que deseas eliminar esta mesa?')) {
      this.adminTablesService.delete(id).subscribe({
        next: () => this.loadTables()
      });
    }
  }

  private emptyTable(): TableAdminRequest {
    return {
      tableNumber: '',
      name: '',
      capacity: 2,
      status: TableStatus.LIBRE
    };
  }
}

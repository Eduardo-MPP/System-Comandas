import { TableStatus } from './table-status.enum';

export interface TableAdminResponse {
  id: number;
  tableNumber: string;   // String para cuadrar con backend
  name: string | null;
  capacity: number;
  status: TableStatus;
}

export interface TableAdminRequest {
  tableNumber: string;
  name: string | null;
  capacity: number;
  status: TableStatus;
}

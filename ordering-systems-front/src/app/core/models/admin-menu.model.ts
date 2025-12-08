export interface MenuItemRequest {
  name: string;
  description: string;
  price: number;
  categoryId: number;
  available: boolean;
  stock: number | null;
}

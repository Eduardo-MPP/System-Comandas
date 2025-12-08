import { RoleType } from './user.model';

export interface UserAdminRequest {
  fullName: string;
  email: string;
  password: string;
  role: RoleType;
  active: boolean;
}

export interface UserAdminResponse {
  id: number;
  fullName: string;
  email: string;
  role: RoleType;
  active: boolean;
  createdAt: string | null;
}

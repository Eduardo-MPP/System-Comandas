    export interface User {
        id: number;
        username: string;
        fullName: string;
        role: RoleType;
    }

    export enum RoleType {
        MESERO = 'MESERO',
        COCINERO = 'COCINERO',
        CAJERO = 'CAJERO',
        ADMIN = 'ADMIN'
    }

    export interface LoginRequest {
        username: string;
        password: string;
    }

    export interface LoginResponse {
        id: number;
        token: string;
        username: string;
        fullName: string;
        role: RoleType;
    }
    
    export interface User {
    id: number;
    username: string;
    fullName: string;
    email?: string;  // ← AGREGAR ESTA LÍNEA
    role: RoleType;
    }


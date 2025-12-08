import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { MeseroDashboardComponent } from './features/mesero/mesero-dashboard/mesero-dashboard.component';
import { CrearPedidoComponent } from './features/mesero/crear-pedido/crear-pedido.component';
import { CocineroDashboardComponent } from './features/cocinero/cocinero-dashboard/cocinero-dashboard.component';
import { CajeroDashboardComponent } from './features/cajero/cajero-dashboard/cajero-dashboard.component';
import { GenerarFacturaComponent } from './features/cajero/generar-factura/generar-factura.component';
import { HistorialCierresComponent } from './features/cajero/historial-cierres/historial-cierres.component';
import { TransaccionesTurnoComponent } from './features/cajero/transacciones-turno/transacciones-turno.component';
import { DetalleCierreComponent } from './features/cajero/detalle-cierre/detalle-cierre.component';

import { AdminLayoutComponent } from './features/admin/admin-layout/admin-layout.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard/admin-dashboard.component';
import { AdminMenuComponent } from './features/admin/admin-menu/admin-menu.component';
import { AdminUsersComponent } from './features/admin/admin-users/admin-users.component';
import { AdminStatisticsComponent } from './features/admin/admin-statistics/admin-statistics.component';
import { AdminTablesComponent } from './features/admin/admin-tables/admin-tables.component';

import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { RoleType } from './core/models/user.model';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  {
    path: 'mesero/dashboard',
    component: MeseroDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.MESERO }
  },
  {
    path: 'mesero/crear-pedido',
    component: CrearPedidoComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.MESERO }
  },

  {
    path: 'cocinero/dashboard',
    component: CocineroDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.COCINERO }
  },

  {
    path: 'cajero/dashboard',
    component: CajeroDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.CAJERO }
  },
  {
    path: 'cajero/generar-factura',
    component: GenerarFacturaComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.CAJERO }
  },
  {
    path: 'cajero/historial-cierres',
    component: HistorialCierresComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.CAJERO }
  },
  {
    path: 'cajero/transacciones-turno',
    component: TransaccionesTurnoComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.CAJERO }
  },
  {
    path: 'cajero/detalle-cierre/:id',
    component: DetalleCierreComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.CAJERO }
  },

  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: RoleType.ADMIN },
    children: [
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'menu', component: AdminMenuComponent },
      { path: 'tables', component: AdminTablesComponent },
      { path: 'users', component: AdminUsersComponent },
      { path: 'statistics', component: AdminStatisticsComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  { path: '**', redirectTo: '/login' }
];

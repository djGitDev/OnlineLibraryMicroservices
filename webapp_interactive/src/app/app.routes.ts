import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {DashboardComponent} from './pages/dashboard/dashboard';
import {DashboardAdminComponent} from './pages/dashboard-admin/dashboard-admin';

import {AuthGuard} from './auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard-user', component: DashboardComponent, canActivate: [AuthGuard], data: { expectedRole: 'ROLE_USER' } },
  { path: 'dashboard-admin', component: DashboardAdminComponent, canActivate: [AuthGuard], data: { expectedRole: 'ROLE_ADMIN' } }

];

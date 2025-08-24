import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {DashboardUserComponent} from './pages/dashboard-user/dashboard';
import {AdminAddComponent} from './pages/dashboard-admin/add-book/admin-add';

import {AuthGuard} from './auth.guard';
import {DashboardAdminComponent} from './pages/dashboard-admin/dashboard-main/dashboard-admin';
import {AdminRemoveComponent} from './pages/dashboard-admin/remove-book/admin-remove';

export const routes: Routes = [

  //Auth
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  //User
  { path: 'dashboard-user', component: DashboardUserComponent, canActivate: [AuthGuard], data: { expectedRole: 'ROLE_USER' } },
  { path: 'dashboard-fbuser', component: DashboardUserComponent, canActivate: [AuthGuard], data: { expectedRole: 'ROLE_FB-USER' } },

  //Admin
  { path: 'dashboard-admin', component: DashboardAdminComponent, canActivate: [AuthGuard], data: { expectedRole: 'ROLE_ADMIN' } },
  { path: 'admin-add', component: AdminAddComponent, canActivate: [AuthGuard], data: { expectedRole: 'ROLE_ADMIN' } },
  { path: 'admin-remove', component: AdminRemoveComponent, canActivate: [AuthGuard], data: { expectedRole: 'ROLE_ADMIN' } },
];

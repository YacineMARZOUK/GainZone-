import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { loginGuard } from './guards/login.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent), canActivate: [loginGuard] },
  { path: 'register', loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent), canActivate: [loginGuard] },
  {
    path: '',
    loadComponent: () => import('./layouts/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'store', loadComponent: () => import('./pages/store/store.component').then(m => m.StoreComponent), canActivate: [authGuard] },
      { path: 'activities', loadComponent: () => import('./pages/activity/activity.component').then(m => m.ActivityComponent), canActivate: [authGuard] },
      { path: 'coach/classes', loadComponent: () => import('./pages/coach-dashboard/coach-dashboard.component').then(m => m.CoachDashboardComponent), canActivate: [authGuard] },
      { path: 'coach/programs', loadComponent: () => import('./pages/program-manager/program-manager.component').then(m => m.ProgramManagerComponent), canActivate: [authGuard] },
      { path: 'admin/inventory', loadComponent: () => import('./pages/admin-product-list/admin-product-list.component').then(m => m.AdminProductListComponent), canActivate: [authGuard] },
      { path: 'admin/users', loadComponent: () => import('./pages/user-management/user-management.component').then(m => m.UserManagementComponent), canActivate: [authGuard] },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];

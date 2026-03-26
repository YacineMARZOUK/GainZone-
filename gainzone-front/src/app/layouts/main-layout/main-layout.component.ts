import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import {
  LucideAngularModule,
  LayoutDashboard,
  Activity,
  ClipboardList,
  Store,
  Dumbbell,
  PlusCircle,
  Users,
  LogOut,
  Package
} from 'lucide-angular';

interface MenuItem {
  label: string;
  path: string;
  icon: any;
}

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    CommonModule,
    LucideAngularModule
  ],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent implements OnInit {
  role: string | null = '';
  menuItems: MenuItem[] = [];

  // Icon instances mapping for template
  icons = {
    LayoutDashboard,
    Activity,
    ClipboardList,
    Store,
    Dumbbell,
    PlusCircle,
    Users,
    LogOut,
    Package
  };

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.role = this.authService.getRole();
    this.buildMenu();
  }

  buildMenu(): void {
    if (this.role === 'MEMBER') {
      this.menuItems = [
        { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
        { label: 'Activités', path: '/activities', icon: Activity },
        { label: 'Mon Programme', path: '/my-program', icon: ClipboardList },
        { label: 'Boutique', path: '/store', icon: Store },
      ];
    } else if (this.role === 'COACH') {
      this.menuItems = [
        { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
        { label: 'Mes Cours (Séances)', path: '/coach/classes', icon: ClipboardList },
        { label: 'Mes Programmes', path: '/coach/programs', icon: Dumbbell }
      ];
    } else if (this.role === 'ADMIN') {
      this.menuItems = [
        { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
        { label: 'Gestion Utilisateurs', path: '/admin/users', icon: Users },
        { label: 'Inventaire Boutique', path: '/admin/inventory', icon: Package }
      ];
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet, RouterLinkActive } from '@angular/router';
import { LucideAngularModule, Link, Palette, BarChart3, Settings, LogOut, PanelLeft, PanelRight, Bell } from 'lucide-angular';
import {AuthService} from '../../services/auth-service';

@Component({
  selector: 'app-taplink-dashboard',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, LucideAngularModule],
  templateUrl: './taplink-dashboard.html',
  styleUrl: './taplink-dashboard.scss'
})
export class TaplinkDashboard {
  private authService = inject(AuthService);
  private router = inject(Router);

  // --- UI State ---
  isLeftExpanded = true;
  isRightExpanded = false;

  // --- Icons ---
  readonly LinkIcon = Link;
  readonly PaletteIcon = Palette;
  readonly BarChartIcon = BarChart3;
  readonly SettingsIcon = Settings;
  readonly LogOutIcon = LogOut;
  readonly PanelLeftIcon = PanelLeft;
  readonly PanelRightIcon = PanelRight;
  readonly BellIcon = Bell;

  menuItems = [
    { label: 'My Links', icon: this.LinkIcon, route: '/dashboard/links' },
    { label: 'Appearance', icon: this.PaletteIcon, route: '/dashboard/appearance' },
    { label: 'Analytics', icon: this.BarChartIcon, route: '/dashboard/analytics' },
    { label: 'Settings', icon: this.SettingsIcon, route: '/dashboard/settings' }
  ];

  // --- Toggle Methods ---
  toggleLeftSidebar() {
    this.isLeftExpanded = !this.isLeftExpanded;
  }

  toggleRightSidebar() {
    this.isRightExpanded = !this.isRightExpanded;
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => this.router.navigate(['/signin']),
      error: () => this.router.navigate(['/signin'])
    });
  }
}

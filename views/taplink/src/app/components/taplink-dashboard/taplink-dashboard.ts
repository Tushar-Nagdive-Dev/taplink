import {Component, inject, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth-service';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {BarChart3, Link, LogOut, LucideAngularModule, Palette, Settings} from 'lucide-angular';

@Component({
  selector: 'app-taplink-dashboard',
  imports: [
    RouterLink,
    RouterOutlet,
    CommonModule,
    LucideAngularModule
  ],
  templateUrl: './taplink-dashboard.html',
  styleUrl: './taplink-dashboard.scss',
})
export class TaplinkDashboard implements OnInit {

  private authService = inject(AuthService);
  private router = inject(Router);

  readonly LinkIcon = Link;
  readonly PaletteIcon = Palette;
  readonly BarChartIcon = BarChart3;
  readonly SettingsIcon = Settings;
  readonly LogOutIcon = LogOut;

  menuItems = [
    { label: 'My Links', icon: this.LinkIcon, route: '/dashboard/links' },
    { label: 'Appearance', icon: this.PaletteIcon, route: '/dashboard/appearance' },
    { label: 'Analytics', icon: this.BarChartIcon, route: '/dashboard/analytics' },
    { label: 'Settings', icon: this.SettingsIcon, route: '/dashboard/settings' }
  ];

  ngOnInit(): void {

  }
  logout() {
    this.authService.logout().subscribe({
      next: () => {
        // Clear local state and redirect to the welcome/signin page
        this.router.navigate(['/signin']);
      },
      error: (err) => {
        console.error('Logout failed', err);
        // Force redirect anyway for safety
        this.router.navigate(['/signin']);
      }
    });
  }
}

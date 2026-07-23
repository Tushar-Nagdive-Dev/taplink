import {Component, inject, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth-service';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-taplink-dashboard',
  imports: [
    RouterLink,
    RouterOutlet,
    CommonModule
  ],
  templateUrl: './taplink-dashboard.html',
  styleUrl: './taplink-dashboard.scss',
})
export class TaplinkDashboard implements OnInit {

  private authService = inject(AuthService);
  private router = inject(Router);

  menuItems = [
    { label: 'My Links', icon: '🔗', route: '/dashboard/links' },
    { label: 'Appearance', icon: '🎨', route: '/dashboard/appearance' },
    { label: 'Analytics', icon: '📊', route: '/dashboard/analytics' },
    { label: 'Settings', icon: '⚙️', route: '/dashboard/settings' }
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

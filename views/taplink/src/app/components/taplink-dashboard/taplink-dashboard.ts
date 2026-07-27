import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet, RouterLinkActive } from '@angular/router';
import { LucideAngularModule, Link, Palette, QrCode, LogOut, PanelLeft, PanelRight, Bell } from 'lucide-angular';
import { AuthService } from '../../services/auth-service';
import { UserProfile } from '../user-profile/user-profile';

@Component({
  selector: 'app-taplink-dashboard',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, LucideAngularModule, UserProfile],
  templateUrl: './taplink-dashboard.html',
  styleUrl: './taplink-dashboard.scss'
})
export class TaplinkDashboard {
  private authService = inject(AuthService);
  private router = inject(Router);
  isProfileModalOpen: boolean = false;

  // --- UI State ---
  isLeftExpanded = true;
  isRightExpanded = false;

  // --- Icons ---
  readonly LinkIcon = Link;
  readonly PaletteIcon = Palette;
  readonly QrCodeIcon = QrCode; // Replaced Analytics/Settings with QR Code
  readonly LogOutIcon = LogOut;
  readonly PanelLeftIcon = PanelLeft;
  readonly PanelRightIcon = PanelRight;
  readonly BellIcon = Bell;

  // --- Cleaned up Menu ---
  menuItems = [
    { label: 'My Links', icon: this.LinkIcon, route: '/taplink-dashboard/links' },
    { label: 'Appearance', icon: this.PaletteIcon, route: '/taplink-dashboard/appearance' },
    { label: 'Share QR', icon: this.QrCodeIcon, route: '/taplink-dashboard/qr' }
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

  openProfileModal() {
    this.isProfileModalOpen = true;
  }

  closeProfileModal() {
    this.isProfileModalOpen = false;
  }
}

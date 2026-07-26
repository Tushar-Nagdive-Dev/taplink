import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ActivatedRoute} from '@angular/router';
import {AppConstants} from '../../constants/app.constants';

@Component({
  selector: 'app-auth-error',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="error-view fade-in-up">
      <div class="error-icon">⚠️</div>
      <h2 class="view-title">Authentication Error</h2>
      <p class="view-subtitle">{{ errorMessage }}</p>
      <a routerLink="/signin" class="macos-btn primary" style="text-decoration: none; text-align: center;">Return to Sign In</a>
    </div>`,
  styles: [`
    .error-view { display: flex; flex-direction: column; align-items: center; justify-content: center; text-align: center; padding: 40px; }
    .error-icon { font-size: 48px; margin-bottom: 16px; }
    .view-title { font-size: 24px; font-weight: 700; color: #1d1d1f; margin-bottom: 8px; }
    .view-subtitle { font-size: 14px; color: #515154; margin-bottom: 32px; }
    .macos-btn { padding: 12px 24px; border-radius: 10px; font-weight: 600; background: #007aff; color: white; display: inline-block; width: 100%; max-width: 200px; }
    .fade-in-up { animation: fadeInUp 0.4s ease forwards; }
    @keyframes fadeInUp { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
  `]
})
export class AuthErrorComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  errorMessage: string = AppConstants.SOMETHING_WENT_WRONG;

  ngOnInit(): void {
    const reason = this.route.snapshot.queryParamMap.get('reason');
    if (reason === 'session_expired') this.errorMessage = AppConstants.SESSION_EXPIRED;
    if (reason === 'login_failed') this.errorMessage = AppConstants.CHECK_USERNAME_AND_PASSWORD;
    if (reason === 'registration_failed') this.errorMessage = AppConstants.USERNAME_ALREADY_TAKEN;
  }
}

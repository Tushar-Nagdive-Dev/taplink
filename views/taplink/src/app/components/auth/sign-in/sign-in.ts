import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {REGEX_CONTRACT} from '../../../constants/regex.constants';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {ILoginRequest} from '../../../interfaces/auth.interface';
import {AuthService} from '../../../services/auth-service';
import {Loader} from '../../loader/loader';
import {ToastService} from '../../../services/toast-service';
import {AppConstants} from '../../../constants/app.constants';
import {Toast} from '../../toast/toast';
import {switchMap} from 'rxjs';

@Component({
  selector: 'app-sign-in',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    RouterLink,
    Loader
  ],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.scss',
})
export class SignIn implements OnInit {
  signInForm: FormGroup = new FormGroup({});
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.signInForm = this.fb.group({
      username: ['', [Validators.required, Validators.pattern(REGEX_CONTRACT.USERNAME_PATTERN)]],
      password: ['', [Validators.required, Validators.pattern(REGEX_CONTRACT.PASSWORD)]],
    });
  }

  onSubmit(): void {
    if (this.signInForm.invalid) {
      this.signInForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const requestPayload: ILoginRequest = {
      username: this.signInForm.value.username,
      password: this.signInForm.value.password
    };

    this.authService.login(requestPayload).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.toastService.show(AppConstants.LOGIN_SUCCESSFUL, AppConstants.TOAST_TYPE.SUCCESS);
        this.router.navigate(['/taplink-dashboard']);
      },
      error: (err) => {
        this.isLoading = false;
        this.toastService.show(AppConstants.CHECK_USERNAME_AND_PASSWORD, AppConstants.TOAST_TYPE.ERROR);
        this.router.navigate(['/auth-error'], { queryParams: { reason: 'login_failed' }});
      }
    });
  }

  loginWithProvider(provider: string) {
    console.log(`Initiating OAuth login with: ${provider}`);
    // OAuth implementation goes here
  }
}

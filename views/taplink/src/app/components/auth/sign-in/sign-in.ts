import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {REGEX_CONTRACT} from '../../../constants/regex.constants';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {ILoginRequest} from '../../../interfaces/auth.interface';
import {AuthService} from '../../../services/auth-service';
import {Loader} from '../../loader/loader';

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
    private router: Router
  ) {}

  ngOnInit(): void {
    this.signInForm = this.fb.group({
      username: ['', [Validators.required, Validators.pattern(REGEX_CONTRACT.EMAIL_PATTERN)]],
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
        console.log('Login successful:', response.authMessage);
        this.isLoading = false;

        // Secure HttpOnly cookie is set! Send them to the dashboard.
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Login failed:', err);
        this.errorMessage = err.error?.message || 'Invalid username or password.';
      }
    });
  }

  loginWithProvider(provider: string) {
    console.log(`Initiating OAuth login with: ${provider}`);
    // OAuth implementation goes here
  }
}

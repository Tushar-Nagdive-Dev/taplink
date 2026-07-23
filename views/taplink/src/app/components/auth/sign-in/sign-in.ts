import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {REGEX_CONTRACT} from '../../../constants/regex.constants';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-sign-in',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    RouterLink
  ],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.scss',
})
export class SignIn implements OnInit {
  signInForm: FormGroup = new FormGroup({});

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.signInForm = this.fb.group({
      username: ['', Validators.required, Validators.pattern(REGEX_CONTRACT.EMAIL_PATTERN)],
      password: ['', Validators.required, Validators.pattern(REGEX_CONTRACT.PASSWORD)],
    })
  }

  onSubmit(): void {
    if (this.signInForm.valid) {
      console.log('Email Login:', this.signInForm.value);
    } else {
      this.signInForm.markAllAsTouched();
    }
  }

  loginWithProvider(provider: string) {
    console.log(`Initiating OAuth login with: ${provider}`);
    // Later, you will call your AuthService here to redirect to Spring Boot's OAuth2 endpoints
  }
}

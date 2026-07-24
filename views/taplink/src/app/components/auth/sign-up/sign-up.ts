import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {REGEX_CONTRACT} from '../../../constants/regex.constants';
import {RouterLink} from '@angular/router';
import {IRegisterRequest} from '../../../interfaces/auth.interface';
import {AuthService} from '../../../services/auth-service';
import {Loader} from '../../loader/loader';

@Component({
  selector: 'app-sign-up',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    RouterLink,
    Loader
  ],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.scss',
})
export class SignUp implements OnInit{

  signUpForm: FormGroup = new FormGroup({});
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.signUpForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      username: ['', [Validators.required, Validators.pattern(REGEX_CONTRACT.USERNAME_PATTERN)]],
      email: ['', [Validators.required, Validators.pattern(REGEX_CONTRACT.EMAIL_PATTERN)]],
      password: ['', [Validators.required, Validators.pattern(REGEX_CONTRACT.PASSWORD)]]
    });
  }

  onSubmit(): void {
    if(this.signUpForm.invalid) {
      this.signUpForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const requestPayload: IRegisterRequest = {
      firstName: this.signUpForm.value.firstName,
      lastName: this.signUpForm.value.lastName,
      username: this.signUpForm.value.username,
      email: this.signUpForm.value.email,
      password: this.signUpForm.value.password
    }

    this.authService.register(requestPayload).subscribe({
      next: (response) => {
        console.log('Registration successful: ', response.authMessage);
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Registration error: ', err);
        this.errorMessage = err.error?.message || 'An error occurred during registration. Please try again.';
      }
    })
  }
}

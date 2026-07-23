import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {REGEX_CONTRACT} from '../../../constants/regex.constants';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-sign-up',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    RouterLink
  ],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.scss',
})
export class SignUp implements OnInit{

  signUpForm: FormGroup = new FormGroup({});

  constructor(
    private fb: FormBuilder,
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
    if(this.signUpForm.valid) {
      console.log('New User Registration Data ready for Spring Boot:', this.signUpForm.value);
    }else {
      this.signUpForm.markAllAsTouched();
    }
  }
}

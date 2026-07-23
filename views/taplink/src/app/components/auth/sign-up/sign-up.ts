import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-sign-up',
  imports: [
    ReactiveFormsModule,
    CommonModule
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
      username: ['', [Validators.required, Validators.pattern()]],
    });
  }
}

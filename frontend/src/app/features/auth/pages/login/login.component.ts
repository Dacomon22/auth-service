import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { ErrorResponse } from '../../../../core/models/error-respones.model';
import { AuthService } from '../../../../core/services/auth.service';
import { LoginRequest } from '../../../../core/models/login-request.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  hide = true;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  onSubmit(): void {
  if (this.loginForm.invalid) {
    this.loginForm.markAllAsTouched();
    this.errorMessage = 'Por favor completa correctamente los campos.';
    this.successMessage = '';
    return;
  }

  this.isLoading = true;
  this.errorMessage = '';
  this.successMessage = '';

  const request: LoginRequest = {
    email: this.loginForm.controls.email.value ?? '',
    password: this.loginForm.controls.password.value ?? ''
  };

  this.authService.login(request).subscribe({
    next: (response) => {
      this.authService.saveToken(response.token);
      this.successMessage = 'Inicio de sesión exitoso.';
      this.errorMessage = '';
      this.isLoading = false;

      console.log('Login success:', response);
    },
    error: (error) => {
      this.isLoading = false;
      this.successMessage = '';

      const backendError = error.error as ErrorResponse | undefined;

      if (backendError?.message) {
        this.errorMessage = backendError.message;
      } else if (error.status === 0) {
        this.errorMessage = 'No fue posible conectar con el servidor.';
      } else {
        this.errorMessage = 'Ocurrió un error al iniciar sesión.';
      }

      console.error('Login error:', error);
    }
  });
  }

  loginWithSso(): void {
    this.authService.redirectToSso();
  }
}
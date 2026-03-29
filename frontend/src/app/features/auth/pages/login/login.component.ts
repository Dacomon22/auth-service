import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';

import { AuthService } from '../../../../core/services/auth.service';
import { LoginRequest } from '../../../../core/models/login-request.model';
import { ErrorResponse } from '../../../../core/models/error-response.model';
import { AuthLayoutComponent } from '../../../../shared/ui/organisms/auth-layout/auth-layout.component';
import { LoginFormComponent } from '../../../../shared/ui/molecules/login-form/login-form.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AuthLayoutComponent,
    LoginFormComponent
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  hide = true;
  isLoading = false;
  successMessage = '';
  errorMessage = '';

  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService
  ) {}

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.errorMessage = 'Por favor completa correctamente los campos.';
      this.successMessage = '';
      return;
    }

    this.isLoading = true;
    this.resetMessages();

    const request: LoginRequest = {
      email: this.loginForm.controls.email.value,
      password: this.loginForm.controls.password.value
    };

    this.authService.login(request)
      .pipe(finalize(() => {
        this.isLoading = false;
      }))
      .subscribe({
        next: (response) => {
          this.authService.saveToken(response.token);
          this.successMessage = 'Inicio de sesión exitoso.';
        },
        error: (error) => {
          this.errorMessage = this.extractErrorMessage(
            error,
            'Ocurrió un error al iniciar sesión.'
          );
        }
      });
  }

  loginWithSso(): void {
    this.resetMessages();

    this.authService.startSso().subscribe({
      next: (response) => {
        this.simulateSsoProvider(response.url);
      },
      error: (error) => {
        this.errorMessage = this.extractErrorMessage(
          error,
          'Error iniciando SSO.'
        );
      }
    });
  }

  private simulateSsoProvider(url: string): void {
    const redirectUri = new URL(url).searchParams.get('redirect_uri') ?? '/sso/callback';
    const isSuccess = confirm('¿Simular SSO exitoso?');
    const code = isSuccess ? 'valid-code' : 'invalid-code';

    window.location.href = `${redirectUri}?code=${code}`;
  }

  private resetMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  private extractErrorMessage(error: unknown, fallbackMessage: string): string {
    const backendError = (error as { error?: ErrorResponse })?.error;
    return backendError?.message || fallbackMessage;
  }
}
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ErrorResponse } from '../../../../core/models/error-response.model';

@Component({
  selector: 'app-sso-callback',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sso-callback.component.html',
  styleUrls: ['./sso-callback.component.scss']
})
export class SsoCallbackComponent implements OnInit {
  successMessage = '';
  errorMessage = '';
  isLoading = true;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    const code = this.route.snapshot.queryParamMap.get('code');

    if (!code) {
      this.isLoading = false;
      this.errorMessage = 'No se recibió el código de autorización.';
      return;
    }

    this.authService.handleSsoCallback(code).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        this.successMessage = 'Autenticación SSO exitosa.';
        this.errorMessage = '';
        this.isLoading = false;

        console.log('SSO success:', response);
      },
      error: (error) => {
        const backendError = error.error as ErrorResponse | undefined;
        this.errorMessage = backendError?.message || 'Error procesando autenticación SSO.';
        this.successMessage = '';
        this.isLoading = false;

        console.error('SSO callback error:', error);
      }
    });
  }
}
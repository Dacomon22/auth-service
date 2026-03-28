import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ErrorResponse } from '../../../../core/models/error-respones.model';

@Component({
  selector: 'app-sso-callback',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sso-callback.component.html',
  styleUrls: ['./sso-callback.component.scss']
})
export class SsoCallbackComponent implements OnInit {
  errorMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    const code = this.route.snapshot.queryParamMap.get('code');

    if (!code) {
      this.errorMessage = 'No se recibió el código de autorización.';
      return;
    }

    this.authService.handleSsoCallback(code).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        console.log('SSO success:', response);
        this.router.navigate(['/']);
      },
      error: (error) => {
        const backendError = error.error as ErrorResponse | undefined;
        this.errorMessage = backendError?.message || 'Error procesando autenticación SSO.';
        console.error('SSO callback error:', error);
      }
    });
  }
}
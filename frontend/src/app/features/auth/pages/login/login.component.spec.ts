import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';
import { AuthService } from '../../../../core/services/auth.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jest.Mocked<AuthService>;

  beforeEach(async () => {
    authService = {
      login: jest.fn(),
      startSso: jest.fn(),
      handleSsoCallback: jest.fn(),
      saveToken: jest.fn(),
      getToken: jest.fn(),
      clearSession: jest.fn()
    } as unknown as jest.Mocked<AuthService>;

    await TestBed.configureTestingModule({
      imports: [LoginComponent,NoopAnimationsModule],
      providers: [
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.restoreAllMocks();
    localStorage.clear();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show validation error when form is invalid', () => {
    component.onSubmit();

    expect(component.loginForm.invalid).toBe(true);
    expect(component.errorMessage).toBe('Por favor completa correctamente los campos.');
    expect(authService.login).not.toHaveBeenCalled();
  });

  it('should call login service when form is valid', () => {
    authService.login.mockReturnValue(of({ token: 'jwt-token', type: 'Bearer' }));

    component.loginForm.setValue({
      email: 'test@test.com',
      password: '123456'
    });

    component.onSubmit();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@test.com',
      password: '123456'
    });
    expect(authService.saveToken).toHaveBeenCalledWith('jwt-token');
    expect(component.successMessage).toBe('Inicio de sesión exitoso.');
    expect(component.errorMessage).toBe('');
    expect(component.isLoading).toBe(false);
  });

  it('should show backend error message on login error', () => {
    authService.login.mockReturnValue(
      throwError(() => ({
        error: {
          code: 'AUTH_001',
          message: 'Invalid credentials'
        }
      }))
    );

    component.loginForm.setValue({
      email: 'test@test.com',
      password: 'wrong-password'
    });

    component.onSubmit();

    expect(component.errorMessage).toBe('Invalid credentials');
    expect(component.successMessage).toBe('');
    expect(component.isLoading).toBe(false);
  });

  it('should call startSso and show error when it fails', () => {
    authService.startSso.mockReturnValue(
      throwError(() => ({
        error: {
          code: 'SSO_001',
          message: 'Error iniciando SSO'
        }
      }))
    );

    component.loginWithSso();

    expect(component.errorMessage).toBe('Error iniciando SSO');
  });
});
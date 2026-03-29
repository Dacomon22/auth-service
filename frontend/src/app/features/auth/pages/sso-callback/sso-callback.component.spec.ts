import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { SsoCallbackComponent } from './sso-callback.component';
import { AuthService } from '../../../../core/services/auth.service';

describe('SsoCallbackComponent', () => {
  let component: SsoCallbackComponent;
  let fixture: ComponentFixture<SsoCallbackComponent>;
  let authService: jest.Mocked<AuthService>;

  const buildRoute = (code: string | null) => ({
    snapshot: {
      queryParamMap: convertToParamMap(code ? { code } : {})
    }
  });

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
      imports: [SsoCallbackComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: ActivatedRoute, useValue: buildRoute('valid-code') }
      ]
    }).compileComponents();
  });

  afterEach(() => {
    jest.restoreAllMocks();
    localStorage.clear();
  });

  it('should create', () => {
    fixture = TestBed.createComponent(SsoCallbackComponent);
    component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  it('should show error when code is missing', () => {
    TestBed.overrideProvider(ActivatedRoute, { useValue: buildRoute(null) });

    fixture = TestBed.createComponent(SsoCallbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.errorMessage).toBe('No se recibió el código de autorización.');
    expect(component.isLoading).toBe(false);
    expect(authService.handleSsoCallback).not.toHaveBeenCalled();
  });

  it('should process callback and save token on success', () => {
    authService.handleSsoCallback.mockReturnValue(of({ token: 'sso-jwt', type: 'Bearer' }));

    fixture = TestBed.createComponent(SsoCallbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(authService.handleSsoCallback).toHaveBeenCalledWith('valid-code');
    expect(authService.saveToken).toHaveBeenCalledWith('sso-jwt');
    expect(component.successMessage).toBe('Autenticación SSO exitosa.');
    expect(component.errorMessage).toBe('');
    expect(component.isLoading).toBe(false);
  });

  it('should show backend error message when callback fails', () => {
    authService.handleSsoCallback.mockReturnValue(
      throwError(() => ({
        error: {
          code: 'SSO_001',
          message: 'Invalid authorization code'
        }
      }))
    );

    fixture = TestBed.createComponent(SsoCallbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Invalid authorization code');
    expect(component.successMessage).toBe('');
    expect(component.isLoading).toBe(false);
  });
});
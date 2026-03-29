import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const authUrl = `${environment.apiUrl}/auth`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call login endpoint', () => {
    const request = {
      email: 'test@test.com',
      password: '123456'
    };

    const mockResponse = {
      token: 'jwt-token',
      type: 'Bearer'
    };

    service.login(request).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${authUrl}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);

    req.flush(mockResponse);
  });

  it('should call startSso endpoint', () => {
    const mockResponse = {
      url: 'https://fake-sso-provider.com/oauth/authorize?...'
    };

    service.startSso().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${authUrl}/sso`);
    expect(req.request.method).toBe('GET');

    req.flush(mockResponse);
  });

  it('should call sso callback endpoint with code', () => {
    const mockResponse = {
      token: 'sso-token',
      type: 'Bearer'
    };

    service.handleSsoCallback('valid-code').subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(
      r => r.url === `${authUrl}/sso/callback` && r.params.get('code') === 'valid-code'
    );

    expect(req.request.method).toBe('GET');

    req.flush(mockResponse);
  });

  it('should save token in localStorage', () => {
    service.saveToken('abc123');
    expect(localStorage.getItem('token')).toBe('abc123');
  });

  it('should return token from localStorage', () => {
    localStorage.setItem('token', 'stored-token');
    expect(service.getToken()).toBe('stored-token');
  });

  it('should clear session', () => {
    localStorage.setItem('token', 'stored-token');
    service.clearSession();
    expect(localStorage.getItem('token')).toBeNull();
  });
});
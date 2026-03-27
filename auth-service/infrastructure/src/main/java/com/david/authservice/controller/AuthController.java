package com.david.authservice.controller;

import com.david.authservice.exception.SsoValidationException;
import com.david.authservice.model.AuthResponse;
import com.david.authservice.model.LoginRequestDTO;
import com.david.authservice.service.AuthService;
import com.david.authservice.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDTO request) {

        log.info("Login request received for username: {}", request.getUsername());
        AuthResponse response = authService.login(request);

        log.info("Login request processed successfully for username: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sso")
    public ResponseEntity<Void> redirectToSso() {

        log.info("Starting SSO flow");

        String clientId = "auth-service-client";

        String redirectUri = "http://localhost:4200/sso/callback";

        String ssoUrl = "https://fake-sso-provider.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        log.debug("Generated SSO URL: {}", ssoUrl);

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, ssoUrl)
                .build();
    }

    @GetMapping("/sso/callback")
    public ResponseEntity<AuthResponse> callback(@RequestParam(required = false) String code) {

        log.info("Received SSO callback request");

        if (code == null || code.trim().isEmpty()) {
            log.warn("SSO failed: missing authorization code");
            throw new SsoValidationException("Missing authorization code");
        }

        log.debug("Received SSO code: {}", code);

        if (!"valid-code".equals(code)) {
            log.warn("SSO failed: invalid authorization code");
            throw new SsoValidationException("Invalid authorization code");
        }

        String username = "sso-user";

        log.info("SSO authentication successful for user: {}", username);

        String token = tokenService.generateToken(username);

        log.info("JWT generated for SSO user: {}", username);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}

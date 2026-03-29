package com.david.authservice.controller;

import com.david.authservice.config.SsoProperties;
import com.david.authservice.model.AuthResponse;
import com.david.authservice.model.LoginRequestDTO;
import com.david.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final SsoProperties ssoProperties;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDTO request) {

        log.info("Login request received for username: {}", request.getEmail());
        AuthResponse response = authService.login(request);

        log.info("Login request processed successfully for username: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sso")
    public ResponseEntity<Map<String, String>> redirectToSso() {

        log.info("Starting SSO flow");

        String ssoUrl = ssoProperties.getAuthorizeUrl()
                + "?client_id=" + ssoProperties.getClientId()
                + "&redirect_uri=" + ssoProperties.getRedirectUri()
                + "&response_type=code";

        log.debug("Generated SSO URL: {}", ssoUrl);

        return ResponseEntity.ok(Collections.singletonMap("url", ssoUrl));
    }

    @GetMapping("/sso/callback")
    public ResponseEntity<AuthResponse> callback(@RequestParam(required = false) String code) {

        log.info("Received SSO callback request");

        AuthResponse response = authService.callback(code,ssoProperties.getMockUser());

        return ResponseEntity.ok(response);
    }
}

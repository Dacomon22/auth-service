package com.david.authservice.persistence.controller;

import com.david.authservice.controller.AuthController;
import com.david.authservice.exception.GlobalExceptionHandler;
import com.david.authservice.model.AuthResponse;
import com.david.authservice.model.LoginRequestDTO;
import com.david.authservice.service.AuthService;
import com.david.authservice.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenService tokenService;

    @Test
    void shouldReturn200AndTokenWhenLoginIsSuccessful() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("david@test.com");
        request.setPassword("123456");

        when(authService.login(any()))
                .thenReturn(new AuthResponse("jwt-token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void shouldReturn400WhenUsernameIsBlank() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("");
        request.setPassword("123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_001"));
    }

    @Test
    void shouldRedirectToSsoProvider() throws Exception {
        mockMvc.perform(get("/api/auth/sso"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", containsString("https://fake-sso-provider.com/oauth/authorize")));
    }

    @Test
    void shouldReturnTokenWhenSsoCodeIsValid() throws Exception {
        when(tokenService.generateToken("sso-user")).thenReturn("jwt-token");

        mockMvc.perform(get("/api/auth/sso/callback")
                        .param("code", "valid-code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void shouldReturn400WhenSsoCodeIsMissing() throws Exception {
        mockMvc.perform(get("/api/auth/sso/callback"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("SSO_001"))
                .andExpect(jsonPath("$.message").value("Missing authorization code"));
    }

    @Test
    void shouldReturn400WhenSsoCodeIsInvalid() throws Exception {
        mockMvc.perform(get("/api/auth/sso/callback")
                        .param("code", "wrong-code"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("SSO_001"))
                .andExpect(jsonPath("$.message").value("Invalid authorization code"));
    }
}

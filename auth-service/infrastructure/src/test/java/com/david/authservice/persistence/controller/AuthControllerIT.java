package com.david.authservice.persistence.controller;

import com.david.authservice.persistence.entity.UserEntity;
import com.david.authservice.persistence.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setUsername("david");
        user.setPassword(passwordEncoder.encode("123456"));
        userJpaRepository.save(user);
    }

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"david\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void shouldReturn401WhenPasswordIsInvalid() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"david\",\"password\":\"wrong-password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_001"));
    }

    @Test
    void shouldRedirectToSsoProviderInIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/auth/sso"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", containsString("https://fake-sso-provider.com/oauth/authorize")));
    }

    @Test
    void shouldReturnTokenWhenValidSsoCodeIsProvided() throws Exception {
        mockMvc.perform(get("/api/auth/sso/callback")
                        .param("code", "valid-code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void shouldReturn400WhenSsoCodeIsInvalidInIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/auth/sso/callback")
                        .param("code", "invalid-code"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("SSO_001"));
    }
}

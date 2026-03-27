package com.david.authservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String type;

    public AuthResponse(String token) {
        this.token = token;
        this.type = "Bearer";
    }
}

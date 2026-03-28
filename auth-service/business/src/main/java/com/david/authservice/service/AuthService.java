package com.david.authservice.service;

import com.david.authservice.model.AuthResponse;
import com.david.authservice.model.LoginRequestDTO;

public interface AuthService {

    AuthResponse login(LoginRequestDTO loginRequestDTO);

    AuthResponse callback(String code, String username);

}

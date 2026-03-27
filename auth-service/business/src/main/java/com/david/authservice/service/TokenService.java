package com.david.authservice.service;


public interface TokenService {
    String generateToken(String username);
}

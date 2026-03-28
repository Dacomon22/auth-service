package com.david.authservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sso")
@Data
public class SsoProperties {

    private String clientId;
    private String redirectUri;
    private String authorizeUrl;
    private String validCode;
    private String mockUser;

}
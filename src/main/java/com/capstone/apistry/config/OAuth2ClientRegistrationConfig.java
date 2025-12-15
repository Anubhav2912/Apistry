package com.capstone.apistry.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * Manually configure OAuth2 Client Registration only when GOOGLE_CLIENT_ID is provided.
 * This replaces the auto-configuration that we excluded to prevent validation errors.
 * Bean is only created when GOOGLE_CLIENT_ID environment variable is set and not empty.
 */
@Configuration
public class OAuth2ClientRegistrationConfig {

    @Bean
    @ConditionalOnExpression("T(java.lang.System).getenv('GOOGLE_CLIENT_ID') != null && !T(java.lang.System).getenv('GOOGLE_CLIENT_ID').isEmpty()")
    public ClientRegistrationRepository clientRegistrationRepository() {
        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
        
        // Create Google OAuth2 client registration
        ClientRegistration googleRegistration = ClientRegistration
                .withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret != null ? clientSecret : "")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("email", "profile")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("email")
                .clientName("Google")
                .build();
        
        return new InMemoryClientRegistrationRepository(googleRegistration);
    }
}


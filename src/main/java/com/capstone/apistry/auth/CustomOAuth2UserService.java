package com.capstone.apistry.auth;

import com.capstone.apistry.entities.User;
import com.capstone.apistry.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * OAuth2 User Service - only created when GOOGLE_CLIENT_ID environment variable is set.
 * Checks environment variable directly since we're not using Spring properties for OAuth2 config.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnExpression("T(java.lang.System).getenv('GOOGLE_CLIENT_ID') != null && !T(java.lang.System).getenv('GOOGLE_CLIENT_ID').isEmpty()")
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // Extract user information from OAuth2 provider (Google)
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String sub = (String) attributes.get("sub"); // Google's unique user ID
        
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not provided by Google");
        }
        
        // Check if user exists by email first
        User user = userService.findByEmail(email)
                .orElseGet(() -> {
                    // Generate a unique username from email
                    String baseUsername = email.split("@")[0];
                    String username = baseUsername;
                    int suffix = 1;
                    
                    // Ensure username is unique
                    while (userService.findByUsername(username).isPresent()) {
                        username = baseUsername + suffix;
                        suffix++;
                    }
                    
                    // Create new user from OAuth2 data
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setEmail(email);
                    // Generate a secure random password (OAuth users won't use password login)
                    newUser.setPassword(passwordEncoder.encode("OAUTH2_" + UUID.randomUUID().toString()));
                    return userService.saveUser(newUser);
                });
        
        // Create a map with the username for Spring Security authentication
        Map<String, Object> userAttributes = new HashMap<>(attributes);
        userAttributes.put("username", user.getUsername());
        userAttributes.put("email", user.getEmail());
        
        // Return OAuth2User with username as the name attribute
        // Spring Security will use getName() which will return user.getUsername()
        DefaultOAuth2User oauth2User = new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("USER")),
                userAttributes,
                "username" // This tells DefaultOAuth2User.getName() to return userAttributes.get("username")
        );
        
        return oauth2User;
    }
}


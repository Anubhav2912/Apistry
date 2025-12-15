package com.capstone.apistry.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.ObjectProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final ApplicationContext applicationContext;
    
    @Lazy
    @Autowired(required = false)
    private CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Only configure OAuth2 if Google client ID is provided and ClientRegistrationRepository exists
        boolean oauth2Configured = isGoogleOAuthConfigured();
        
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                // Only permit OAuth2 endpoints if OAuth2 is configured, otherwise let controller handle it
                .requestMatchers("/oauth2/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(userDetailsService)
            .csrf(csrf -> csrf.disable());

        // Only configure OAuth2 login if Google client ID is provided and ClientRegistrationRepository exists
        // Configure OAuth2 to use the same login page as form login so both methods appear together
        if (oauth2Configured) {
            http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Use custom login page (same as form login)
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .userInfoEndpoint(userInfo -> {
                    if (oAuth2UserService != null) {
                        userInfo.userService(oAuth2UserService);
                    }
                })
            );
        }

        return http.build();
    }

    private boolean isGoogleOAuthConfigured() {
        // Check if Google OAuth credentials are provided via environment variable
        String envClientId = System.getenv("GOOGLE_CLIENT_ID");
        if (envClientId == null || envClientId.isEmpty()) {
            return false;
        }
        
        // Check if ClientRegistrationRepository bean exists (it's only created when credentials are provided)
        // Use getBeanProvider to safely check without throwing exception if bean doesn't exist
        try {
            ObjectProvider<ClientRegistrationRepository> beanProvider = 
                applicationContext.getBeanProvider(ClientRegistrationRepository.class);
            ClientRegistrationRepository repo = beanProvider.getIfAvailable();
            if (repo == null) {
                return false;
            }
            return repo.findByRegistrationId("google") != null;
        } catch (Exception e) {
            // Bean doesn't exist, OAuth2 not configured
            return false;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

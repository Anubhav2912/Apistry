package com.capstone.apistry.controllers;

import com.capstone.apistry.entities.User;
import com.capstone.apistry.services.UserService;
import com.capstone.apistry.services.WorkspaceService;
import com.capstone.apistry.services.CollectionService;
import com.capstone.apistry.services.ApiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final WorkspaceService workspaceService;
    private final CollectionService collectionService;
    private final ApiRequestService apiRequestService;
    private final ApplicationContext applicationContext;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("googleOAuthEnabled", isGoogleOAuthEnabled());
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("googleOAuthEnabled", isGoogleOAuthEnabled());
        return "register";
    }
    
    /**
     * Handle OAuth2 authorization when not configured - prevent redirect loops
     */
    @GetMapping("/oauth2/authorization/google")
    public String handleOAuth2NotConfigured(RedirectAttributes redirectAttributes) {
        if (!isGoogleOAuthEnabled()) {
            redirectAttributes.addFlashAttribute("error", 
                "Google OAuth is not configured. Please set GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET environment variables and restart the application.");
            return "redirect:/register";
        }
        // If configured, Spring Security will handle this
        return "redirect:/login";
    }
    
    private boolean isGoogleOAuthEnabled() {
        // Check if Google OAuth credentials are provided via environment variable
        String envClientId = System.getenv("GOOGLE_CLIENT_ID");
        if (envClientId == null || envClientId.isEmpty()) {
            return false;
        }
        
        // Check if ClientRegistrationRepository bean exists
        try {
            ClientRegistrationRepository repo = applicationContext.getBean(ClientRegistrationRepository.class);
            return repo != null && repo.findByRegistrationId("google") != null;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             RedirectAttributes redirectAttributes) {
        
        // Check if username already exists
        if (userService.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/register";
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        
        userService.saveUser(user);
        
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get data for the dashboard
        var workspaces = workspaceService.getAllWorkspaces();
        var collections = collectionService.getAllCollections();

        // Scope requests (history & saved) to the current authenticated user
        User currentUser = getCurrentUser();
        var requests = currentUser != null
                ? apiRequestService.getRecentApiRequestsForUser(currentUser)
                : java.util.Collections.emptyList();
        var savedRequests = currentUser != null
                ? apiRequestService.getSavedApiRequestsForUser(currentUser)
                : java.util.Collections.emptyList();
        
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("collections", collections);
        model.addAttribute("requests", requests);
        model.addAttribute("savedRequests", savedRequests);
        
        return "dashboard";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        String username = authentication.getName();
        return userService.findByUsername(username).orElse(null);
    }
}

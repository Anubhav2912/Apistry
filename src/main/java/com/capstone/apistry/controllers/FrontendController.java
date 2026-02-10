package com.capstone.apistry.controllers;

import com.capstone.apistry.entities.*;
import com.capstone.apistry.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class FrontendController {

    private final WorkspaceService workspaceService;
    private final CollectionService collectionService;
    private final ApiRequestService apiRequestService;
    private final EnvironmentService environmentService;
    private final UserService userService;

    @GetMapping("/request/new")
    public String newRequest(Model model) {
    
        ApiRequest request = ApiRequest.builder()
                .method("GET")
                .url("")
                .headers("{}")
                .body("")
                .build();
    
        model.addAttribute("request", request);
    
        User currentUser = getCurrentUser();
    
        if (currentUser != null && currentUser.getId() != null) {
            model.addAttribute(
                "historyRequests",
                apiRequestService.getRecentApiRequestsForUser(currentUser)
            );
            model.addAttribute(
                "savedRequests",
                apiRequestService.getSavedApiRequestsForUser(currentUser)
            );
        } else {
            model.addAttribute("historyRequests", java.util.Collections.emptyList());
            model.addAttribute("savedRequests", java.util.Collections.emptyList());
        }
    
        return "request";
    }

    @GetMapping("/collection/new")
    public String newCollection(Model model) {
        Collection collection = Collection.builder()
                .name("New Collection")
                .build();
        model.addAttribute("collection", collection);
        model.addAttribute("requests", Collections.emptyList());
        return "collection";
    }

    @GetMapping("/workspace/new")
    public String newWorkspace(Model model) {
        Workspace workspace = Workspace.builder()
                .name("New Workspace")
                .build();
        model.addAttribute("workspace", workspace);
        model.addAttribute("collections", Collections.emptyList());
        model.addAttribute("environments", Collections.emptyList());
        return "workspace";
    }

    @GetMapping("/workspace/{id}")
    public String workspace(@PathVariable Long id, Model model) {
        Workspace workspace = workspaceService.getWorkspaceById(id);
        List<Collection> collections = collectionService.getAllCollections();
        List<Environment> environments = environmentService.getAllEnvironments();

        model.addAttribute("workspace", workspace);
        model.addAttribute("collections", collections);
        model.addAttribute("environments", environments);
        return "workspace";
    }

    @GetMapping("/collection/{id}")
    public String collection(@PathVariable Long id, Model model) {
        Collection collection = collectionService.getCollectionById(id);
        List<ApiRequest> requests = apiRequestService.getAllApiRequests();

        model.addAttribute("collection", collection);
        model.addAttribute("requests", requests);
        return "collection";
    }

    @GetMapping("/request/{id}")
    public String request(@PathVariable Long id, Model model) {
        ApiRequest request = apiRequestService.getApiRequestById(id);
        model.addAttribute("request", request);

        // Populate recent request history and saved requests for the current user
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            var history = apiRequestService.getRecentApiRequestsForUser(currentUser);
            var saved = apiRequestService.getSavedApiRequestsForUser(currentUser);
            model.addAttribute("historyRequests", history);
            model.addAttribute("savedRequests", saved);
        }
        return "request";
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

    @GetMapping("/environment-management")
    public String environments(Model model) {
        List<Environment> environments = environmentService.getAllEnvironments();
        model.addAttribute("environments", environments);
        return "environments";
    }
}


package com.capstone.apistry.controllers;

import com.capstone.apistry.entities.ApiRequest;
import com.capstone.apistry.entities.User;
import com.capstone.apistry.services.ApiRequestService;
import com.capstone.apistry.services.UserService;
import com.capstone.apistry.domain.ExecuteResponse;
import com.capstone.apistry.entities.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ApiRequestController {

    private final ApiRequestService apiRequestService;
    private final UserService userService;

    @PostMapping
    public ApiRequest create(@RequestBody ApiRequest request) {
        attachCurrentUser(request);
        return apiRequestService.saveApiRequest(request);
    }

    @GetMapping("/{id}")
    public ApiRequest getById(@PathVariable Long id) {
        return apiRequestService.getApiRequestById(id);
    }

    @GetMapping
    public List<ApiRequest> getAll() {
        return apiRequestService.getAllApiRequests();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        apiRequestService.deleteApiRequest(id);
    }

    @PostMapping("/execute")
    public ExecuteResponse execute(@RequestBody ApiRequest request) {
        long startedAt = System.currentTimeMillis();
        // Attach owner and persist the request so it shows up in history
        attachCurrentUser(request);
        ApiRequest persisted = apiRequestService.saveApiRequest(request);

        ApiResponse executed = apiRequestService.executeRequest(persisted);
        long elapsed = System.currentTimeMillis() - startedAt;

        // Parse headers JSON if possible; otherwise return empty map
        try {
            Map<String, String> headersMap = new ObjectMapper()
                    .readValue(executed.getHeaders(), new TypeReference<Map<String, String>>() {});
            return new ExecuteResponse(
                    executed.getStatusCode(),
                    executed.getBody() == null ? "" : executed.getBody(),
                    headersMap,
                    elapsed
            );
        } catch (Exception e) {
            return new ExecuteResponse(
                    executed.getStatusCode(),
                    executed.getBody() == null ? "" : executed.getBody(),
                    java.util.Collections.emptyMap(),
                    elapsed
            );
        }
    }

    private void attachCurrentUser(ApiRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return;
        }
        String username = authentication.getName();
        userService.findByUsername(username).ifPresent(request::setOwner);
    }
}

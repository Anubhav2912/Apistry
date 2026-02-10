package com.capstone.apistry.services.impl;

import com.capstone.apistry.entities.ApiRequest;
import com.capstone.apistry.entities.ApiResponse;
import com.capstone.apistry.entities.EnvironmentVariable;
import com.capstone.apistry.entities.User;
import com.capstone.apistry.repositories.ApiRequestRepository;
import com.capstone.apistry.repositories.EnvironmentVariableRepository;
import com.capstone.apistry.repositories.ApiResponseRepository;
import com.capstone.apistry.services.ApiRequestService;
import com.capstone.apistry.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ApiRequestServiceImpl implements ApiRequestService {

    private final ApiRequestRepository apiRequestRepository;
    private final EnvironmentVariableRepository environmentVariableRepository;
    private final HttpClientUtil httpClientUtil;
    private final ApiResponseRepository apiResponseRepository;

    @Override
    public ApiRequest saveApiRequest(ApiRequest request) {
        return apiRequestRepository.save(request);
    }

    @Override
    public ApiRequest getApiRequestById(Long id) {
        return apiRequestRepository.findById(id).orElse(null);
    }

    @Override
    public List<ApiRequest> getAllApiRequests() {
        return apiRequestRepository.findAll();
    }

    @Override
    public void deleteApiRequest(Long id) {
        apiRequestRepository.deleteById(id);
    }

    @Override
    public List<ApiRequest> getRecentApiRequestsForUser(User owner) {
        return apiRequestRepository.findTop10ByOwnerOrderByIdDesc(owner);
    }

    @Override
    public List<ApiRequest> getSavedApiRequestsForUser(User owner) {
        return apiRequestRepository.findByOwnerAndSavedTrueOrderByIdDesc(owner);
    }

    @Override
    public void clearHistoryForUser(User owner) {
        if (owner == null) {
            return;
        }
        // Instead of hard-deleting rows (which can hit FK constraints),
        // simply detach history requests from the user so they disappear
        // from all user-scoped queries.
        List<ApiRequest> historyRequests = apiRequestRepository.findByOwnerAndSavedFalse(owner);
        if (historyRequests.isEmpty()) {
            return;
        }
        for (ApiRequest r : historyRequests) {
            r.setOwner(null);
        }
        apiRequestRepository.saveAll(historyRequests);
    }

    @Override
    public void clearSavedForUser(User owner) {
        if (owner == null) {
            return;
        }
        // Detach saved requests from the user and mark them as not saved,
        // so they disappear from both saved and history lists.
        List<ApiRequest> savedRequests = apiRequestRepository.findByOwnerAndSavedTrueOrderByIdDesc(owner);
        if (savedRequests.isEmpty()) {
            return;
        }
        for (ApiRequest r : savedRequests) {
            r.setOwner(null);
            r.setSaved(false);
        }
        apiRequestRepository.saveAll(savedRequests);
    }

    public ApiResponse executeRequest(ApiRequest apiRequest) {
        // 1) Substitute environment variables like {{BASE_URL}}
        List<EnvironmentVariable> vars = environmentVariableRepository.findAll();
        String resolvedUrl = apiRequest.getUrl();
        for (EnvironmentVariable var : vars) {
            String key = var.getKeyName();
            String value = var.getValue();
            if (key != null && value != null) {
                resolvedUrl = resolvedUrl.replace("{{" + key + "}}", value);
            }
        }

        // 2) Prepare headers/body/method
        Map<String, String> headers = parseHeaders(apiRequest.getHeaders());
        HttpMethod method = safeHttpMethod(apiRequest.getMethod());

        // 3) Execute via HttpClientUtil.sendRequest(...)
        ResponseEntity<String> resp = httpClientUtil.sendRequest(
                resolvedUrl,
                method,
                headers,
                apiRequest.getBody()
        );

        // 4) Map to ApiResponse entity (not persisted here)
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(resp.getStatusCode().value());
        apiResponse.setBody(resp.getBody());
        try {
            String headersJson = new ObjectMapper().writeValueAsString(resp.getHeaders().toSingleValueMap());
            apiResponse.setHeaders(headersJson);
        } catch (Exception e) {
            // Fallback to simple toString if JSON serialization fails
            apiResponse.setHeaders(resp.getHeaders().toSingleValueMap().toString());
        }
        apiResponse.setRequest(apiRequest);

        return apiResponse;
    }

    private Map<String, String> parseHeaders(String json) {
        if (json == null || json.isBlank()) return Collections.emptyMap();
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            // If the stored string wasn't valid JSON, ignore and send without headers
            return Collections.emptyMap();
        }
    }

    private HttpMethod safeHttpMethod(String m) {
        if (m == null || m.isBlank()) return HttpMethod.GET;
        try {
            return HttpMethod.valueOf(m.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return HttpMethod.GET;
        }
    }
}

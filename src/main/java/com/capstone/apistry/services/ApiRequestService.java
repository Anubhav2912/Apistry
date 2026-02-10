package com.capstone.apistry.services;

import com.capstone.apistry.entities.ApiRequest;
import com.capstone.apistry.entities.ApiResponse;
import com.capstone.apistry.entities.User;

import java.util.List;

public interface ApiRequestService {
    ApiRequest saveApiRequest(ApiRequest request);
    ApiRequest getApiRequestById(Long id);
    List<ApiRequest> getAllApiRequests();
    void deleteApiRequest(Long id);

    /**
     * Returns up to the 10 most recent API requests for the given owner, newest first.
     */
    List<ApiRequest> getRecentApiRequestsForUser(User owner);

    /**
     * Returns all explicitly saved API requests for the given owner.
     */
    List<ApiRequest> getSavedApiRequestsForUser(User owner);

    /**
     * Clears all non-saved history entries for the given owner.
     */
    void clearHistoryForUser(User owner);

    /**
     * Clears all saved requests for the given owner.
     */
    void clearSavedForUser(User owner);

    ApiResponse executeRequest(ApiRequest apiRequest);
}

package com.capstone.apistry.repositories;

import com.capstone.apistry.entities.ApiResponse;
import com.capstone.apistry.entities.ApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiResponseRepository extends JpaRepository<ApiResponse, Long> {
    Optional<ApiResponse> findByRequest(ApiRequest request);

    /**
     * Deletes all responses associated with the given requests.
     */
    void deleteByRequestIn(List<ApiRequest> requests);
}


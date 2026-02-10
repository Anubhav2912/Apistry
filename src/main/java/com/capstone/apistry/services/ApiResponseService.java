package com.capstone.apistry.services;

import com.capstone.apistry.entities.ApiResponse;
import java.util.List;
import java.util.Optional;

public interface ApiResponseService {
    ApiResponse save(ApiResponse apiResponse);
    Optional<ApiResponse> findById(Long id);
    List<ApiResponse> findAll();
    void delete(Long id);
}

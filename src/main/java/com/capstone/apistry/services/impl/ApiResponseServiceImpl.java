package com.capstone.apistry.services.impl;

import com.capstone.apistry.entities.ApiResponse;
import com.capstone.apistry.repositories.ApiResponseRepository;
import com.capstone.apistry.services.ApiResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiResponseServiceImpl implements ApiResponseService {

    private final ApiResponseRepository apiResponseRepository;

    @Override
    public ApiResponse save(ApiResponse apiResponse) {
        return apiResponseRepository.save(apiResponse);
    }

    @Override
    public Optional<ApiResponse> findById(Long id) {
        return apiResponseRepository.findById(id);
    }

    @Override
    public List<ApiResponse> findAll() {
        return apiResponseRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        apiResponseRepository.deleteById(id);
    }
}

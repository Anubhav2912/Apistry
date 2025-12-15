package com.capstone.apistry.controllers;

import com.capstone.apistry.entities.ApiResponse;
import com.capstone.apistry.services.ApiResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
public class ApiResponseController {

    private final ApiResponseService apiResponseService;

    @PostMapping
    public ApiResponse saveResponse(@RequestBody ApiResponse apiResponse) {
        return apiResponseService.save(apiResponse);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse> getResponseById(@PathVariable Long id) {
        return apiResponseService.findById(id);
    }

    @GetMapping
    public List<ApiResponse> getAllResponses() {
        return apiResponseService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteResponse(@PathVariable Long id) {
        apiResponseService.delete(id);
    }
}

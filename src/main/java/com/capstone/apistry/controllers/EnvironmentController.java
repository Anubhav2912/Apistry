package com.capstone.apistry.controllers;

import com.capstone.apistry.entities.EnvironmentVariable;
import com.capstone.apistry.services.EnvironmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/environments")
@RequiredArgsConstructor
public class EnvironmentController {

    private final EnvironmentService environmentService;

    @PostMapping
    public EnvironmentVariable create(@RequestBody EnvironmentVariable variable) {
        return environmentService.saveVariable(variable);
    }

    @GetMapping("/{id}")
    public EnvironmentVariable getById(@PathVariable Long id) {
        return environmentService.getVariableById(id);
    }

    @GetMapping
    public List<EnvironmentVariable> getAll() {
        return environmentService.getAllVariables();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        environmentService.deleteVariable(id);
    }
}

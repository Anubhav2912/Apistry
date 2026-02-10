package com.capstone.apistry.services.impl;

import com.capstone.apistry.entities.Environment;
import com.capstone.apistry.entities.EnvironmentVariable;
import com.capstone.apistry.repositories.EnvironmentRepository;
import com.capstone.apistry.repositories.EnvironmentVariableRepository;
import com.capstone.apistry.services.EnvironmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvironmentServiceImpl implements EnvironmentService {

    private final EnvironmentRepository environmentRepository;
    private final EnvironmentVariableRepository environmentVariableRepository;

    // Environment methods
    @Override
    public Environment saveEnvironment(Environment environment) {
        return environmentRepository.save(environment);
    }

    @Override
    public Environment getEnvironmentById(Long id) {
        return environmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Environment> getAllEnvironments() {
        return environmentRepository.findAll();
    }

    @Override
    public void deleteEnvironment(Long id) {
        environmentRepository.deleteById(id);
    }

    // EnvironmentVariable methods
    @Override
    public EnvironmentVariable saveVariable(EnvironmentVariable variable) {
        return environmentVariableRepository.save(variable);
    }

    @Override
    public EnvironmentVariable getVariableById(Long id) {
        return environmentVariableRepository.findById(id).orElse(null);
    }

    @Override
    public List<EnvironmentVariable> getAllVariables() {
        return environmentVariableRepository.findAll();
    }

    @Override
    public void deleteVariable(Long id) {
        environmentVariableRepository.deleteById(id);
    }
}

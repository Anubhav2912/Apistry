package com.capstone.apistry.services;

import com.capstone.apistry.entities.Environment;
import com.capstone.apistry.entities.EnvironmentVariable;
import java.util.List;

public interface EnvironmentService {
    // Environment methods
    Environment saveEnvironment(Environment environment);
    Environment getEnvironmentById(Long id);
    List<Environment> getAllEnvironments();
    void deleteEnvironment(Long id);
    
    // EnvironmentVariable methods (keeping for backward compatibility)
    EnvironmentVariable saveVariable(EnvironmentVariable variable);
    EnvironmentVariable getVariableById(Long id);
    List<EnvironmentVariable> getAllVariables();
    void deleteVariable(Long id);
}

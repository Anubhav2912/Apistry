package com.capstone.apistry.utils;

import com.capstone.apistry.entities.EnvironmentVariable;
import com.capstone.apistry.repositories.EnvironmentVariableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EnvironmentVariableResolver {

    private final EnvironmentVariableRepository environmentVariableRepository;

    public String resolve(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        List<EnvironmentVariable> vars = environmentVariableRepository.findAll();
        String resolved = input;

        for (EnvironmentVariable var : vars) {
            String key = var.getKeyName();  // <-- Correct field name
            String value = var.getValue();

            if (key != null && value != null) {
                resolved = resolved.replace("{{" + key + "}}", value);
            }
        }
        return resolved;
    }
}


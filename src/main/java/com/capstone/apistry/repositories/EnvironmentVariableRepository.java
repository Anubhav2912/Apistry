package com.capstone.apistry.repositories;

import com.capstone.apistry.entities.EnvironmentVariable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentVariableRepository extends JpaRepository<EnvironmentVariable, Long> {}

package com.capstone.apistry.repositories;

import com.capstone.apistry.entities.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {}

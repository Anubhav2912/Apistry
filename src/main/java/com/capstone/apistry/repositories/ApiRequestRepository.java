package com.capstone.apistry.repositories;

import com.capstone.apistry.entities.ApiRequest;
import com.capstone.apistry.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {

    /**
     * Returns the 10 most recent API requests (by id, descending).
     */
    List<ApiRequest> findTop10ByOwnerOrderByIdDesc(User owner);

    /**
     * Returns all explicitly saved requests for a given owner, newest first.
     */
    List<ApiRequest> findByOwnerAndSavedTrueOrderByIdDesc(User owner);

    /**
     * Returns all non-saved (history) requests for a given owner.
     */
    List<ApiRequest> findByOwnerAndSavedFalse(User owner);

    /**
     * Deletes all non-saved (transient history) requests for a given owner.
     */
    void deleteByOwnerAndSavedFalse(User owner);

    /**
     * Deletes all saved requests for a given owner.
     */
    void deleteByOwnerAndSavedTrue(User owner);
}

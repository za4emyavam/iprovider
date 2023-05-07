package com.example.iprovider.data;

import com.example.iprovider.entities.AdditionalService;

import java.util.Optional;

/**
 * Interface for working with additional services.
 */
public interface AdditionalServiceRepository {
    /**
     * Read an additional service by its ID.
     *
     * @param additionalServiceId the ID of the additional service
     * @return an Optional containing the additional service, or an empty Optional if the additional service was not found
     */
    Optional<AdditionalService> read(Long additionalServiceId);

    /**
     * Read all additional services.
     *
     * @return an Iterable containing all additional services
     */
    Iterable<AdditionalService> readAll();

    /**
     * Create a new additional service.
     *
     * @param additionalService the additional service to create
     * @return the created additional service
     */
    AdditionalService create(AdditionalService additionalService);

    /**
     * Update an existing additional service.
     *
     * @param additionalService the additional service to update
     * @return the updated additional service
     */
    AdditionalService update(AdditionalService additionalService);

    /**
     * Delete an additional service by its ID.
     *
     * @param additionalServiceId the ID of the additional service to delete
     * @return true if the additional service was successfully deleted, false otherwise
     */
    boolean delete(Long additionalServiceId);
}

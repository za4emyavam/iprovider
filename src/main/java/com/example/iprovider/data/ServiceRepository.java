package com.example.iprovider.data;

import com.example.iprovider.entities.Service;

import java.util.Optional;

/**
 * Interface for working with services.
 */
public interface ServiceRepository {
    /**
     * Create a new service.
     *
     * @param service the service to create
     * @return the created service
     */
    Service create(Service service);

    /**
     * Read all services.
     *
     * @return an Iterable containing all services, or an empty Iterable if no services were found
     */
    Iterable<Service> readAll();

    /**
     * Find a service by its ID.
     *
     * @param id the ID of the service to find
     * @return an Optional containing the service, or an empty Optional if the service was not found
     */
    Optional<Service> findById(Long id);

    /**
     * Delete a service by its ID.
     *
     * @param serviceId the ID of the service to delete
     * @return true if the service was successfully deleted, false otherwise
     */
    boolean delete(Long serviceId);
}

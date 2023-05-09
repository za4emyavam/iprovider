package com.example.iprovider.data;

import com.example.iprovider.entities.Tariff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface for working with tariffs.
 */
public interface TariffRepository {
    /**
     * Read all tariffs.
     *
     * @return an Iterable containing all tariffs, or an empty Iterable if no tariffs were found
     */
    Iterable<Tariff> readAll();

    /**
     * Read all tariffs with pagination.
     *
     * @param page the page number to retrieve
     * @param size the number of tariffs to retrieve per page
     * @return an Iterable containing the requested tariffs, or an empty Iterable if no tariffs were found
     */
    Iterable<Tariff> readAll(int page, int size);
    Page<Tariff> readAll(Pageable page);

    /**
     * Get the total number of tariffs.
     *
     * @return the number of tariffs
     */
    Integer count();

    /**
     * Create a new tariff.
     *
     * @param tariff the tariff to create
     * @return the created tariff
     */
    Tariff create(Tariff tariff);

    /**
     * Read a tariff by its ID.
     *
     * @param tariffId the ID of the tariff to read
     * @return an Optional containing the tariff, or an empty Optional if the tariff was not found
     */
    Optional<Tariff> readByID(Long tariffId);

    /**
     * Update an existing tariff.
     *
     * @param tariff the tariff to update
     * @return the updated tariff
     */
    Tariff update(Tariff tariff);

    /**
     * Delete a tariff by its ID.
     *
     * @param tariffId the ID of the tariff to delete
     * @return true if the tariff was successfully deleted, false otherwise
     */
    boolean delete(Long tariffId);

    /**
     * Read all tariffs for a given service.
     *
     * @param serviceId the ID of the service
     * @return an Iterable containing all tariffs for the service, or an empty Iterable if no tariffs were found
     */
    Iterable<Tariff> readAllByService(Long serviceId);
}

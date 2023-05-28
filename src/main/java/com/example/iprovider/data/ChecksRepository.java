package com.example.iprovider.data;

import com.example.iprovider.entities.Checks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface for working with checks.
 */
public interface ChecksRepository {
    /**
     * Read all checks with pagination.
     *
     * @param page the page number (starting from 0)
     * @param size the number of checks to return per page
     * @return an Iterable containing the checks on the specified page, or an empty Iterable if no checks were found
     */
    Iterable<Checks> readAll(int page, int size);
    Page<Checks> readAll(PageRequest pageRequest);

    /**
     * Get the total number of checks.
     *
     * @return the total number of checks
     */
    Integer getAmount();

    /**
     * Create a new check.
     *
     * @param adminId the ID of the admin creating the check
     * @return the created check
     */
    Checks create(Long adminId);

    /**
     * Read the last created check.
     *
     * @return an Optional containing the last created check, or an empty Optional if no checks were found
     */
    Optional<Checks> readLast();
}

package com.example.iprovider.data;

import com.example.iprovider.entities.ConnectionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface for working with connection requests.
 */
public interface ConnectionRequestRepository {
    /**
     * Create a new connection request.
     *
     * @param connectionRequest the connection request to create
     * @return the created connection request
     */
    ConnectionRequest create(ConnectionRequest connectionRequest);

    /**
     * Read a connection request by its ID.
     *
     * @param connectionRequestId the ID of the connection request
     * @return an Optional containing the connection request, or an empty Optional if the connection request was not found
     */
    Optional<ConnectionRequest> read(Long connectionRequestId);

    /**
     * Update an existing connection request.
     *
     * @param connectionRequest the connection request to update
     * @return the updated connection request
     */

    ConnectionRequest update(ConnectionRequest connectionRequest);

    /**
     * Read all connection requests.
     *
     * @return an Iterable containing all connection requests, or an empty Iterable if no connection requests were found
     */
    Iterable<ConnectionRequest> readAll();
    Page<ConnectionRequest> readAll(PageRequest pageRequest);

    /**
     * Read all connection requests with pagination.
     *
     * @param page the page number (starting from 0)
     * @param size the number of connection requests to return per page
     * @return an Iterable containing the connection requests on the specified page, or an empty Iterable if no connection requests were found
     */
    Iterable<ConnectionRequest> readAll(int page, int size);

    /**
     * Read all connection requests for a given subscriber.
     *
     * @param subscriberId the ID of the subscriber
     * @return an Iterable containing all connection requests for the subscriber, or an empty Iterable if no connection requests were found
     */
    Iterable<ConnectionRequest> readAllBySubscriber(Long subscriberId);

    /**
     * Delete a connection request by its ID.
     *
     * @param connectionRequestId the ID of the connection request to delete
     * @return true if the connection request was successfully deleted, false otherwise
     */
    boolean delete(Long connectionRequestId);

    /**
     * Get the total number of connection requests.
     *
     * @return the total number of connection requests
     */
    Integer getAmount();
}

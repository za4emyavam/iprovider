package com.example.iprovider.data;

import com.example.iprovider.entities.RequestAdditionalServices;

import java.util.Optional;

/**
 * Interface for working with requests for additional services.
 */
public interface RequestAdditionalServicesRepository {
    /**
     * Create a new request for additional services.
     *
     * @param requestAdditionalServices the request for additional services to create
     * @return the created request for additional services
     */
    RequestAdditionalServices create(RequestAdditionalServices requestAdditionalServices);

    /**
     * Read a request for additional services by its ID.
     *
     * @param requestAdditionalServicesId the ID of the request for additional services
     * @return an Optional containing the request for additional services, or an empty Optional if the request was not found
     */
    Optional<RequestAdditionalServices> read(Long requestAdditionalServicesId);

    /**
     * Read all requests for additional services for a given connection request.
     *
     * @param connectionRequestId the ID of the connection request
     * @return an Iterable containing all requests for additional services for the connection request, or an empty Iterable if no requests were found
     */
    Iterable<RequestAdditionalServices> readByConnectionRequestId(Long connectionRequestId);

    /**
     * Read all requests for additional services for a given subscriber.
     *
     * @param subscriberId the ID of the subscriber
     * @return an Iterable containing all requests for additional services for the subscriber, or an empty Iterable if no requests were found
     */
    Iterable<RequestAdditionalServices> readBySubscriberId(Long subscriberId);

    /**
     * Delete a request for additional services by its ID.
     *
     * @param requestAdditionalServicesId the ID of the request for additional services to delete
     * @return true if the request for additional services was successfully deleted, false otherwise
     */
    boolean delete(Long requestAdditionalServicesId);
}

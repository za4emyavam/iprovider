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

     Reads a request additional services record from the data source by the specified request ID and service ID.
     @param requestId The ID of the request to search for.
     @param serviceId The ID of the service to search for.
     @return An Optional containing the found request additional services record or empty if not found.
     */
    Optional<RequestAdditionalServices> read(Long requestId, Long serviceId);

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

     Deletes a request additional services record from the data source by the specified request ID and service ID.
     @param requestId The ID of the request to search for.
     @param serviceId The ID of the service to search for.
     @return true if the record was deleted successfully, false otherwise.
     */
    boolean delete(Long requestId, Long serviceId);
}

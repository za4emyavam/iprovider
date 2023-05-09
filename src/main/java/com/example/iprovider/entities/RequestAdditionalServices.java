package com.example.iprovider.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A data class representing a request for additional services.
 * <p>
 * Each instance contains information about the connection request ID, additional service ID, and status.
 * <p>
 * The status can be one of two values: "expected" or "paid".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAdditionalServices {
    /**
     * The connection request ID associated with this request.
     */
    private ConnectionRequest requestId;
    /**
     * The additional service ID associated with this request.
     */
    private AdditionalService serviceId;
    /**
     * The status of this request, which can be "expected" or "paid".
     */
    private Status status;

    /**
     * An enumeration representing the two possible values for the status of this request.
     */
    public enum Status {
        expected, paid
    }
}

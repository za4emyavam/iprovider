package com.example.iprovider.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAdditionalServices {
    private Long requestAdditionalServicesId;
    private ConnectionRequest requestId;
    private AdditionalService serviceId;

    private Status status;

    public RequestAdditionalServices(ConnectionRequest requestId, AdditionalService serviceId, Status status) {
        this.requestId = requestId;
        this.serviceId = serviceId;
        this.status = status;
    }

    public enum Status {
        expected, paid
    }
}

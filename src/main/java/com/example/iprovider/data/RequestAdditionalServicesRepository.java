package com.example.iprovider.data;

import com.example.iprovider.entities.RequestAdditionalServices;

import java.util.Optional;

public interface RequestAdditionalServicesRepository {
    RequestAdditionalServices create(RequestAdditionalServices requestAdditionalServices);
    Optional<RequestAdditionalServices> read(Long requestAdditionalServicesId);
    Iterable<RequestAdditionalServices> readByConnectionRequestId(Long connectionRequestId);
    Iterable<RequestAdditionalServices> readBySubscriberId(Long subscriberId);
    boolean delete(Long requestAdditionalServicesId);
}

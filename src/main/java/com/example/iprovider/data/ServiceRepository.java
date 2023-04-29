package com.example.iprovider.data;

import com.example.iprovider.entities.Service;

import java.util.Optional;

public interface ServiceRepository {
    Service create(Service service);
    Iterable<Service> readAll();
    Optional<Service> findById(Long id);
    boolean delete(Long serviceId);
}

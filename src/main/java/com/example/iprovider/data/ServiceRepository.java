package com.example.iprovider.data;

import com.example.iprovider.entities.Service;

import java.util.Optional;

public interface ServiceRepository {
    Iterable<Service> findAll();

    Optional<Service> findById(Long id);
}

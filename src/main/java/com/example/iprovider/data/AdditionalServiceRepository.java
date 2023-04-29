package com.example.iprovider.data;

import com.example.iprovider.entities.AdditionalService;

import java.util.Optional;

public interface AdditionalServiceRepository {
    Optional<AdditionalService> read(Long additionalServiceId);
    Iterable<AdditionalService> readAll();
    AdditionalService create(AdditionalService additionalService);
    AdditionalService update(AdditionalService additionalService);
    boolean delete(Long additionalServiceId);
}

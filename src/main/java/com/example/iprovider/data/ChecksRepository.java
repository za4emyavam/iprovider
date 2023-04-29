package com.example.iprovider.data;

import com.example.iprovider.entities.Checks;

import java.util.Optional;

public interface ChecksRepository {
    Iterable<Checks> readAll(int page, int size);
    Integer getAmount();
    Checks create(Long adminId);
    Optional<Checks> readLast();
}

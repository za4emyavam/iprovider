package com.example.iprovider.data;

import com.example.iprovider.entities.Tariff;

import java.util.Optional;

public interface TariffRepository {
    Iterable<Tariff> readAll();
    Iterable<Tariff> readAll(int page, int size);
    Integer getAmount();
    Tariff create(Tariff tariff);
    Optional<Tariff> readByID(Long tariffId);
    Tariff update(Tariff tariff);
    boolean delete(Long tariffId);
}

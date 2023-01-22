package com.example.iprovider.data;

import com.example.iprovider.model.Tariff;

public interface TariffRepository {
    Iterable<Tariff> readAll();
    Iterable<Tariff> readAll(int page, int size);
    Integer getAmount();
    Iterable<Tariff> readById(Long userId);
}

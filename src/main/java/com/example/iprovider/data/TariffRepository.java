package com.example.iprovider.data;

import com.example.iprovider.model.Tariff;

public interface TariffRepository {
    Iterable<Tariff> findAll();
}

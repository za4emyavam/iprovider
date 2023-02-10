package com.example.iprovider.data;

import com.example.iprovider.entities.UserTariffs;

public interface UserTariffsRepository {
    Iterable<UserTariffs> readById(Long userId);
    boolean deleteByUserIdTariffId(Long userId, Long tariffId);
}

package com.example.iprovider.data;

import com.example.iprovider.entities.UserTariffs;

public interface UserTariffsRepository {
    Iterable<UserTariffs> readById(Long userId);
    Iterable<UserTariffs> readAll();
    Iterable<UserTariffs> readAll(int page, int size);
    Integer getAmount();
    Integer getAmountOfSubscribers();
    boolean deleteByUserIdTariffId(Long userId, Long tariffId);
}

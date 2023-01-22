package com.example.iprovider.data;

import com.example.iprovider.model.UserTariffs;

public interface UserTariffsRepository {
    Iterable<UserTariffs> readById(Long userId);
}

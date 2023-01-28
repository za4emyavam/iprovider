package com.example.iprovider.data;

import com.example.iprovider.entities.User;

import java.util.Optional;

public interface UserRepository {

    User create(User user);
    User findByUsername(String username);
    Optional<User> read(Long userId);
    Iterable<User> readAll(int page, int size);
    User update(User user);
    Integer getAmount();
}

package com.example.iprovider.data;

import com.example.iprovider.model.User;

public interface UserRepository {

    User create(User user);
    User findByUsername(String username);
}

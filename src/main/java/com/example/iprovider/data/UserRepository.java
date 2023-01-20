package com.example.iprovider.data;

import com.example.iprovider.model.User;

public interface UserRepository {

    User findByUsername(String username);
}

package com.example.iprovider.data;

import com.example.iprovider.entities.ConnectionRequest;

public interface ConnectionRequestRepository {
    Iterable<ConnectionRequest> findAll();
}

package com.example.iprovider.data;

import com.example.iprovider.entities.ConnectionRequest;

import java.util.Optional;

public interface ConnectionRequestRepository {
    ConnectionRequest create(ConnectionRequest connectionRequest);
    Optional<ConnectionRequest> read(Long connectionRequestId);
    ConnectionRequest update(ConnectionRequest connectionRequest);
    Iterable<ConnectionRequest> readAll();

    Iterable<ConnectionRequest> readAll(int page, int size);

    Integer getAmount();


}

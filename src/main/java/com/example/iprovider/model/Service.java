package com.example.iprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Service {
    private Long serviceId;
    private final String serviceType;
}

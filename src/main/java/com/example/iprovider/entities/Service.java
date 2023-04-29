package com.example.iprovider.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    private Long serviceId;

    @NotNull
    @Size(min=5, message = "Name must contain at least 4 characters")
    private String serviceType;
}

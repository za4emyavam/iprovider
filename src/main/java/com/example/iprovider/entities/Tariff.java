package com.example.iprovider.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Tariff implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long tariffId;

    @NotNull
    @Size(min=5, message = "Name must contain at least 5 characters")
    private String name;
    @NotNull
    @Size(min=5, message = "Description must contain at least 10 characters")
    private String description;

    @NotNull
    @Positive(message = "Value must be positive")
    private Double cost;

    @NotNull
    @Positive(message = "Value must be positive")
    private Integer frequencyOfPayment;

    @NotNull
    private Status status;

    @Size(min=1, message = "You must choose at least 1 service")
    private List<Service> services;

    public Tariff(String name, String description, Double cost, Integer frequencyOfPayment, Status status, List<Service> services) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.frequencyOfPayment = frequencyOfPayment;
        this.status = status;
        this.services = services;
    }

    public void addService(Service service) {
        this.services.add(service);
    }

    public enum Status {
        DISABLED, ACTIVE
    }

    public List<Long> getIdOfServices() {
        List<Long> results = new ArrayList<>();
        for(Service service : this.services)
            results.add(service.getServiceId());
        return results;
    }
}

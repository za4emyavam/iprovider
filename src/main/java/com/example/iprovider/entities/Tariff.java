package com.example.iprovider.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Tariff implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long tariffId;

    private String name;
    @NotNull
    private String description;

    private Double cost;

    private Integer frequencyOfPayment;

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
}

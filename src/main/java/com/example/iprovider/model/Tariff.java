package com.example.iprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class Tariff implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long tariffId;

    private String name;

    private String description;

    private Double cost;

    private Integer frequencyOfPayment;

    private Status status;
    private List<Service> services;

    public enum Status {
        DISABLED, ACTIVE
    }
}

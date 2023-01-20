package com.example.iprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Tariff implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tariffId;

    private String name;

    private String description;

    private Double cost;

    private Integer frequencyOfPayment;

    private Status status;

    public enum Status {
        DISABLED, ACTIVE
    }
}

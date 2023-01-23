package com.example.iprovider.entities;

import lombok.Data;

@Data
public class CreatingTariffForm {
    private final String name;
    private final String description;
    private final Double cost;
    private final Integer frequencyOfPayment;
    private final String service;

    /*public Tariff toTariff() {
        return new Tariff(
                name, description, cost, frequencyOfPayment, Tariff.Status.ACTIVE,
                Tariff.Status.valueOf(service.toUpperCase())
        );
    }*/
}

package com.example.iprovider.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalService implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long additionalServiceId;

    @NotNull
    @Size(min=5, message = "Name must contain at least 5 characters")
    private String name;

    @NotNull
    @Size(min=5, message = "Description must contain at least 10 characters")
    private String description;

    @NotNull
    @Positive(message = "Value must be positive")
    private Double cost;

    public AdditionalService(String name, String description, Double cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

}

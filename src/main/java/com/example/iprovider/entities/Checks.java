package com.example.iprovider.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checks {
    private Long checkId;
    private Long checkerId;
    private Integer users;
    private Double amount;
    private Date dateOfCheck;
}

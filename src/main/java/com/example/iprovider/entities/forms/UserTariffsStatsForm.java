package com.example.iprovider.entities.forms;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor

public class UserTariffsStatsForm {
    private String address;
    private String tariff;
    private Date from;
    private Date to;
}

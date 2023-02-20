package com.example.iprovider.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserTariffs {
    private Long user_tariffs_id;
    private final User userId;
    private final Tariff tariffId;
    private final Date dateOfStart;
    private final Date dateOfLastPayment;
}

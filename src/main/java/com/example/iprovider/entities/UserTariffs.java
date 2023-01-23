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
    private final Long userId;
    private final Tariff tariff_id;
    private final Date dateOfStart;
    private final Date dateOfLastPayment;
}

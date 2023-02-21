package com.example.iprovider.entities.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortUserStatisticsForm {
    private String address;
    private Long tariff;
    private Date dateOfStartPeriod;
    private Date dateOfEndPeriod;
    public void fixValues() {
        if(address == null)
            address = "";

        if (tariff == null)
            tariff = 0L;

        if (dateOfStartPeriod == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            String dateInString = "2003-01-01";
            try {
                dateOfStartPeriod = formatter.parse(dateInString);
            } catch (ParseException e) {
            }
        }

        if (dateOfEndPeriod == null) {
            dateOfEndPeriod = new Date();
        }
    }
}

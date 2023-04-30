package com.example.iprovider.entities.forms;

import com.example.iprovider.entities.AdditionalService;
import com.example.iprovider.entities.Tariff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequestForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long subscriber;
    private String city;
    private String address;
    private Tariff tariff;
    private List<AdditionalService> additionalServiceList;

    public ConnectionRequestForm(Integer subscriber, String city, String address,
                                 List<AdditionalService> additionalServiceList) {
        this.subscriber = (long) subscriber;
        this.city = city;
        this.address = address;
        this.additionalServiceList = additionalServiceList;
    }
}

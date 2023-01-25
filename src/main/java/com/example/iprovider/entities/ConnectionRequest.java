package com.example.iprovider.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long connectionRequestId;
    private Long subscriber;
    private String city;
    private String address;
    private Tariff tariff;
    private Date dateOfChange;
    private RequestStatusType status;

    public enum RequestStatusType {
        IN_PROCESSING, REJECTED, APPROVED
    }

    public ConnectionRequest(Integer subscriber, String city, String address) {
        this.subscriber = (long)subscriber;
        this.city = city;
        this.address = address;
    }
}

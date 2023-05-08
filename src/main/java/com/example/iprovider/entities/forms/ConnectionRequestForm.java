package com.example.iprovider.entities.forms;

import com.example.iprovider.entities.AdditionalService;
import com.example.iprovider.entities.Tariff;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A class representing a connection request form which includes the subscriber's information,
 * <p>
 * city and address of connection, selected tariff and additional services.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequestForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long subscriber;

    @NotNull
    @NotEmpty(message = "City can't be empty")
    private String city;
    @NotNull
    @NotEmpty(message = "Address can't be empty")
    private String address;

    private Tariff tariff;

    private List<AdditionalService> additionalServiceList;

    /**
     * Constructs a new ConnectionRequestForm with the specified subscriber information,
     * city, address and list of additional services.
     *
     * @param subscriber            The ID of the subscriber who submitted the connection request.
     * @param city                  The city in which the subscriber wants to be connected.
     * @param address               The address of the subscriber where the connection should be established.
     * @param additionalServiceList The list of {@link AdditionalService} selected by the subscriber.
     */
    public ConnectionRequestForm(Long subscriber, String city, String address,
                                 List<AdditionalService> additionalServiceList) {
        this.subscriber = subscriber;
        this.city = city;
        this.address = address;
        this.additionalServiceList = additionalServiceList;
    }

    /**
     * Returns the list of additional service IDs selected by the subscriber for the connection request.
     *
     * @return The list of additional service IDs.
     */
    public List<Long> getAdditionalServicesId() {
        List<Long> res = new ArrayList<>();
        for (AdditionalService additionalService : additionalServiceList)
            res.add(additionalService.getAdditionalServiceId());
        return res;
    }
}

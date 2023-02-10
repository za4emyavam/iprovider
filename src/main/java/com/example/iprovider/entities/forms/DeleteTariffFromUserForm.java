package com.example.iprovider.entities.forms;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeleteTariffFromUserForm {
    @NotNull

    private Long userId;
    @NotNull
    private Long tariffId;

    public DeleteTariffFromUserForm(Long userId, Long tariffId) {
        this.userId = userId;
        this.tariffId = tariffId;
    }

    public DeleteTariffFromUserForm() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public void setTariffId(Long tariffId) {
        this.tariffId = tariffId;
    }
}

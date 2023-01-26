package com.example.iprovider.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long transactionId;
    private Long balanceId;
    private TransactionType type;
    @NotNull
    @Positive(message = "Value must be positive")
    private Double transactionAmount;
    private Date transactionDate;
    private TransactionStatusType transactionStatus;

    public Transaction (Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public enum TransactionType {
        DEBIT, REFILL
    }

    public enum TransactionStatusType {
        SUCCESSFUL, DENIED
    }
}

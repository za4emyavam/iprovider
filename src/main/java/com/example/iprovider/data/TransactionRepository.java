package com.example.iprovider.data;

import com.example.iprovider.entities.Transaction;

public interface TransactionRepository {
    Transaction create(Transaction transaction);
    Iterable<Transaction> readAllByUserBalanceId(long balanceId, int page, int size);

    Integer getAmountByUserBalanceId(long balanceId);
}

package com.example.iprovider.data;

import com.example.iprovider.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface for working with transactions.
 */
public interface TransactionRepository {
    /**
     * Create a new transaction.
     *
     * @param transaction the transaction to create
     * @return the created transaction
     */
    Transaction create(Transaction transaction);

    /**
     * Read all transactions for a user balance with pagination.
     *
     * @param balanceId the ID of the user balance to retrieve transactions for
     * @param page      the page number to retrieve
     * @param size      the number of transactions to retrieve per page
     * @return an Iterable containing the requested transactions, or an empty Iterable if no transactions were found
     */
    Iterable<Transaction> readAllByUserBalanceId(long balanceId, int page, int size);
    Page<Transaction> readAllByUserBalanceId(long balanceId, Pageable page);

    /**
     * Get the total number of transactions for a user balance.
     *
     * @param balanceId the ID of the user balance to retrieve the number of transactions for
     * @return the number of transactions for the user balance
     */
    Integer getAmountByUserBalanceId(long balanceId);
}

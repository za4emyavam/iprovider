package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.TransactionRepository;
import com.example.iprovider.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

@Repository
public class JdbcTransactionRepository implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transaction create(Transaction transaction) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO transaction" +
                "(balance_id, type, transaction_amount, transaction_date, transaction_status) " +
                        "VALUES (?, ?::transaction_type, ?, DEFAULT, ?::transaction_status_type)",
                Types.INTEGER, Types.VARCHAR, Types.NUMERIC, Types.VARCHAR
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        transaction.getBalanceId(),
                        transaction.getType().name().toLowerCase(),
                        transaction.getTransactionAmount(),
                        transaction.getTransactionStatus().name()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(psc, keyHolder);
        long transactionId = Long.parseLong(keyHolder.getKeys().get("transaction_id").toString());
        String transactionStatus = keyHolder.getKeys().get("transaction_status").toString();

        transaction.setTransactionId(transactionId);
        transaction.setTransactionStatus(Transaction.TransactionStatusType.valueOf(transactionStatus));
        return transaction;
    }

    @Override
    public Iterable<Transaction> readAllByUserBalanceId(long balanceId, int page, int size) {
        int offset = (page - 1) * size;
        return jdbcTemplate.query(
                "select * from transaction where balance_id=? order by transaction_date desc " +
                        "limit ? offset ?",
                this::mapRowToTransaction, balanceId, size, offset
        );
    }

    @Override
    public Page<Transaction> readAllByUserBalanceId(long balanceId, Pageable page) {
        List<Transaction> transactionList = jdbcTemplate.query(
                "select * from transaction where balance_id=? order by transaction_date desc " +
                        "limit ? offset ?",
                this::mapRowToTransaction,
                balanceId,
                page.getPageSize(),
                page.getOffset()
        );
        return new PageImpl<>(transactionList, page, getAmountByUserBalanceId(balanceId));
    }

    @Override
    public Integer getAmountByUserBalanceId(long balanceId) {
        return jdbcTemplate.query("select count(transaction_id) from transaction where balance_id=?",
                        (rs, rowNum) -> rs.getInt(1), balanceId)
                .get(0);
    }

    private Transaction mapRowToTransaction(ResultSet rs, int rowNum) throws SQLException {
        return new Transaction(
                rs.getLong("transaction_id"),
                rs.getLong("balance_id"),
                Transaction.TransactionType.valueOf(rs.getString("type").toUpperCase()),
                rs.getDouble("transaction_amount"),
                rs.getDate("transaction_date"),
                Transaction.TransactionStatusType.valueOf(rs.getString("transaction_status"))
        );
    }
}

package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.model.Tariff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcTariffRepository implements TariffRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTariffRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Tariff> findAll() {
        return jdbcTemplate.query("select * from tariff", this::mapToRowTariff);
    }

    private Tariff mapToRowTariff(ResultSet row, int rowNum) throws SQLException {
        return new Tariff(
                row.getLong("tariff_id"),
                row.getString("name"),
                row.getString("description"),
                row.getDouble("cost"),
                row.getInt("frequency_of_payment"),
                Tariff.Status.valueOf(row.getString("status").toUpperCase())
        );
    }
}

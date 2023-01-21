package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.model.Service;
import com.example.iprovider.model.Tariff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcTariffRepository implements TariffRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTariffRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Tariff> readAll() {
        return jdbcTemplate.query("select * from tariff", this::mapToRowTariff);
    }

    @Override
    public Iterable<Tariff> readAll(int page, int size) {
        int offset = (page - 1) * size;
        return jdbcTemplate.query("select * from tariff limit ? offset ?",
                this::mapToRowTariff,
                size,
                offset
        );
    }

    @Override
    public Integer getAmount() {
        return jdbcTemplate.query("select count(tariff_id) from tariff",
                (rs, rowNum) -> rs.getInt(1))
                .get(0);
    }

    private Tariff mapToRowTariff(ResultSet row, int rowNum) throws SQLException {
        return new Tariff(
                row.getLong("tariff_id"),
                row.getString("name"),
                row.getString("description"),
                row.getDouble("cost"),
                row.getInt("frequency_of_payment"),
                Tariff.Status.valueOf(row.getString("status").toUpperCase()),
                tariffServicesByTariffId(row.getLong("tariff_id"))

        );
    }

    private List<Service> tariffServicesByTariffId(long tariffId) {
        return jdbcTemplate.query(
                "select s.* from tariff_services ts, service s " +
                        "where ts.tariff_id=(?) and ts.services_id=s.service_id",
                this::mapToRowService, tariffId);
    }

    private Service mapToRowService(ResultSet row, int rowNum) throws SQLException {
        return new Service(
                row.getLong("service_id"),
                row.getString("service_type")
        );
    }
}

package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Service;
import com.example.iprovider.entities.Tariff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

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

    @Override
    @Transactional
    public Tariff create(Tariff tariff) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into tariff (name, description, cost, frequency_of_payment, status) " +
                        "VALUES (?, ?, ?, ?, ?::tariff_status_type)",
                Types.VARCHAR, Types.VARCHAR, Types.NUMERIC, Types.INTEGER, Types.VARCHAR
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        tariff.getName(),
                        tariff.getDescription(),
                        tariff.getCost(),
                        tariff.getFrequencyOfPayment(),
                        tariff.getStatus().name().toLowerCase()
                ));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        long tariffId = Long.parseLong(keyHolder.getKeys().get("tariff_id").toString());
        for (Service service : tariff.getServices()) {
            addServiceToTariff(tariffId, service);
        }

        tariff.setTariffId(tariffId);

        return tariff;
    }

    @Override
    public Optional<Tariff> readByID(Long tariffId) {
        List<Tariff> results = jdbcTemplate.query("select * from tariff t " +
                "where tariff_id=?",
                this::mapToRowTariff,
                tariffId);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));

    }

    @Override
    public Tariff update(Tariff tariff) {
        jdbcTemplate.update(
                "UPDATE tariff t SET name=?, description=?, cost=?," +
                        " frequency_of_payment=?, status=?::tariff_status_type WHERE tariff_id=?",
                tariff.getName(),
                tariff.getDescription(),
                tariff.getCost(),
                tariff.getFrequencyOfPayment(),
                tariff.getStatus().name().toLowerCase(),
                tariff.getTariffId()
        );
        return tariff;
    }

    @Override
    public boolean delete(Long tariffId) {
        return jdbcTemplate.update(
                "delete from tariff where tariff_id=?",
                tariffId
        ) == 1;
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

    private long addServiceToTariff(long tariffId, Service service) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into tariff_services (tariff_id, services_id) " +
                        "VALUES (?, ?)",
                Types.INTEGER, Types.INTEGER
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        tariffId,
                        service.getServiceId()
                ));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return Long.parseLong(keyHolder.getKeys().get("tariff_services_id").toString());
    }
}

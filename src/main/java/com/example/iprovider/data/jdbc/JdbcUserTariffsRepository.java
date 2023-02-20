package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.UserTariffsRepository;
import com.example.iprovider.entities.Service;
import com.example.iprovider.entities.Tariff;
import com.example.iprovider.entities.UserTariffs;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcUserTariffsRepository implements UserTariffsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcUserRepository jdbcUserRepository;

    public JdbcUserTariffsRepository(JdbcTemplate jdbcTemplate,
                                     JdbcUserRepository jdbcUserRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcUserRepository = jdbcUserRepository;
    }

    @Override
    public Iterable<UserTariffs> readById(Long userId) {
        return jdbcTemplate.query("select * from user_tariffs ut " +
                        "where ut.user_id=?", this::mapRowToUserTariffs,
                userId
        );
    }

    @Override
    public Iterable<UserTariffs> readAll() {
        return jdbcTemplate.query("select * from user_tariffs", this::mapRowToUserTariffs);
    }

    @Override
    public Iterable<UserTariffs> readAll(int page, int size) {
        int offset = (page - 1) * size;
        return jdbcTemplate.query("select * from user_tariffs order by user_tariffs_id limit ? offset ?",
                this::mapRowToUserTariffs, size, offset);
    }

    @Override
    public Integer getAmount() {
        return jdbcTemplate.query("select count(*) from user_tariffs",
                (rs, rowNum) -> rs.getInt(1))
                .get(0);
    }

    @Override
    public Integer getAmountOfSubscribers() {
        return jdbcTemplate.query("select count(distinct user_id) from user_tariffs",
                (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public boolean deleteByUserIdTariffId(Long userId, Long tariffId) {
        return jdbcTemplate.update(
                "delete from user_tariffs where user_id=? and tariff_id=?",
                userId, tariffId
        ) != 0;
    }

    private UserTariffs mapRowToUserTariffs(ResultSet rs, int rowNum) throws SQLException {
        return new UserTariffs(
                rs.getLong("user_tariffs_id"),
                jdbcUserRepository.read(rs.getLong("user_id")).get(),
                tariffById(rs.getLong("tariff_id")),
                rs.getDate("date_of_start"),
                rs.getDate("date_of_last_payment")
        );
    }

    private Tariff tariffById(Long tariffId) {
        List<Tariff> tariffs = jdbcTemplate.query("select * from tariff t " +
                        "where t.tariff_id=?",
                this::mapToRowTariff,
                tariffId);
        return tariffs.isEmpty() ? null : tariffs.get(0);
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

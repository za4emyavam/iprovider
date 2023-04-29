package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.AdditionalServiceRepository;
import com.example.iprovider.entities.AdditionalService;
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
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcAdditionalServiceRepository implements AdditionalServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAdditionalServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<AdditionalService> read(Long additionalServiceId) {
        List<AdditionalService> results = jdbcTemplate.query("select * from additional_service " +
                        "where additional_service_id=?",
                this::mapToRowAdditionalService,
                additionalServiceId);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public Iterable<AdditionalService> readAll() {
        return jdbcTemplate.query("select * from additional_service", this::mapToRowAdditionalService);
    }

    @Override
    public AdditionalService create(AdditionalService additionalService) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into additional_service (name, description, cost) " +
                        "VALUES (?, ?, ?)",
                Types.VARCHAR, Types.VARCHAR, Types.NUMERIC
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        additionalService.getName(),
                        additionalService.getDescription(),
                        additionalService.getCost()
                )
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        long additionalServiceId = Long.parseLong(Objects.requireNonNull(keyHolder.getKeys()).get("additional_service_id").toString());
        additionalService.setAdditionalServiceId(additionalServiceId);
        return additionalService;
    }

    @Override
    public AdditionalService update(AdditionalService additionalService) {
        jdbcTemplate.update(
                "update additional_service set name=?, description=?, cost=? " +
                        "where additional_service_id=?",
                additionalService.getName(),
                additionalService.getDescription(),
                additionalService.getCost(),
                additionalService.getAdditionalServiceId()
        );
        return additionalService;
    }

    @Override
    public boolean delete(Long additionalServiceId) {
        return jdbcTemplate.update(
                "delete from additional_service where additional_service_id=?",
                additionalServiceId
        ) == 1;
    }

    private AdditionalService mapToRowAdditionalService(ResultSet row, int rowNum) throws SQLException {
        return new AdditionalService(
                row.getLong("additional_service_id"),
                row.getString("name"),
                row.getString("description"),
                row.getDouble("cost")
        );
    }
}

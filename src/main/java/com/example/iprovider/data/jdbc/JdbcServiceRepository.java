package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.ServiceRepository;
import com.example.iprovider.entities.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class JdbcServiceRepository implements ServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Service create(Service service) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into service (service_type) " +
                        "VALUES (?)",
                Types.VARCHAR
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Collections.singletonList(
                        service.getServiceType()
                ));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        long serviceId = Long.parseLong(keyHolder.getKeys().get("service_id").toString());
        service.setServiceId(serviceId);

        return service;
    }

    @Override
    public Iterable<Service> readAll() {
        return jdbcTemplate.query(
                "select * from service", this::mapRowToService
        );
    }

    @Override
    public Optional<Service> findById(Long id) {
        List<Service> results = jdbcTemplate.query(
                "select * from service where service_id=?",
                this::mapRowToService,
                id
        );
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public boolean delete(Long serviceId) {
        return jdbcTemplate.update(
                "delete from service where service_id=?",
                serviceId
        ) == 1;
    }

    private Service mapRowToService(ResultSet rs, int rowNum) throws SQLException {
        return new Service(
                rs.getLong("service_id"),
                rs.getString("service_type")
        );
    }
}

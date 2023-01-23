package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.ServiceRepository;
import com.example.iprovider.entities.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
public class JdbcServiceRepository implements ServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Service> findAll() {
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

    private Service mapRowToService(ResultSet rs, int rowNum) throws SQLException {
        return new Service(
                rs.getLong("service_id"),
                rs.getString("service_type")
        );
    }
}

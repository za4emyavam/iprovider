package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.ConnectionRequestRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.ConnectionRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcConnectionRequestRepository implements ConnectionRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    private final TariffRepository tariffRepository;

    public JdbcConnectionRequestRepository(JdbcTemplate jdbcTemplate, TariffRepository tariffRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Iterable<ConnectionRequest> findAll() {
        return jdbcTemplate.query(
                "select cr.* from connection_request cr, tariff t where cr.tariff=t.tariff_id",
                this::mapRowToConnectionRequest
        );
    }

    private ConnectionRequest mapRowToConnectionRequest(ResultSet rs, int rowNum) throws SQLException {
        return new ConnectionRequest(
                rs.getLong("connection_request_id"),
                rs.getLong("subscriber"),
                rs.getString("city"),
                rs.getString("address"),
                tariffRepository.readByID(rs.getLong("tariff")).get(),
                rs.getDate("date_of_change"),
                ConnectionRequest.RequestStatusType.valueOf(rs.getString("status").toUpperCase())
        );
    }
}

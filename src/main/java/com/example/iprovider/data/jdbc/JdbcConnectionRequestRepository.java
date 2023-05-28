package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.ConnectionRequestRepository;
import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.ConnectionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcConnectionRequestRepository implements ConnectionRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    private final TariffRepository tariffRepository;

    public JdbcConnectionRequestRepository(JdbcTemplate jdbcTemplate,
                                           TariffRepository tariffRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Iterable<ConnectionRequest> readAll() {
        return jdbcTemplate.query(
                "select cr.* from connection_request cr, tariff t where cr.tariff=t.tariff_id",
                this::mapRowToConnectionRequest
        );
    }

    @Override
    public Page<ConnectionRequest> readAll(PageRequest pageRequest) {
        List<ConnectionRequest> connectionRequests = jdbcTemplate.query(
                "select * from connection_request " +
                        "order by connection_request_id DESC limit ? offset ?", this::mapRowToConnectionRequest,
                pageRequest.getPageSize(),
                pageRequest.getOffset());
        return new PageImpl<>(connectionRequests, pageRequest, getAmount());
    }

    @Override
    public Iterable<ConnectionRequest> readAll(int page, int size) {
        int offset = (page - 1) * size;
        return jdbcTemplate.query(
                "select * from connection_request " +
                        " limit ? offset ?", this::mapRowToConnectionRequest,
                size, offset);
    }

    @Override
    public Iterable<ConnectionRequest> readAllBySubscriber(Long subscriberId) {
        return jdbcTemplate.query(
                "select * from connection_request where subscriber=(?)",
                this::mapRowToConnectionRequest,
                subscriberId
        );
    }

    @Override
    public boolean delete(Long connectionRequestId) {
        jdbcTemplate.update(
                "delete from request_additional_services where request_id = ?",
                connectionRequestId
        );

        return jdbcTemplate.update(
                "delete from connection_request where connection_request_id=?",
                connectionRequestId
        ) == 1;
    }

    @Override
    public Integer getAmount() {
        return jdbcTemplate.query("select count(connection_request_id) from connection_request",
                        (rs, rowNum) -> rs.getInt(1))
                .get(0);
    }

    @Override
    @Transactional
    public ConnectionRequest create(ConnectionRequest connectionRequest) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into connection_request (subscriber, city, address, tariff, date_of_change, status) " +
                        "values (?, ?, ?, ?, ?, ?::request_status_type)",
                Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.DATE, Types.VARCHAR
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        connectionRequest.getSubscriber(),
                        connectionRequest.getCity(),
                        connectionRequest.getAddress(),
                        connectionRequest.getTariff().getTariffId(),
                        connectionRequest.getDateOfChange(),
                        connectionRequest.getStatus().toString().toLowerCase()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        long connectionRequestId = Long.parseLong(keyHolder.getKeys().get("connection_request_id").toString());
        connectionRequest.setConnectionRequestId(connectionRequestId);
        return connectionRequest;
    }

    @Override
    public Optional<ConnectionRequest> read(Long connectionRequestId) {
        List<ConnectionRequest> results = jdbcTemplate.query(
                "select * from connection_request where connection_request_id=?",
                this::mapRowToConnectionRequest,
                connectionRequestId
        );
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public ConnectionRequest update(ConnectionRequest connectionRequest) {
        jdbcTemplate.update(
                "update connection_request " +
                        "set city=?, address=?, tariff=?, date_of_change=?, status=?::request_status_type " +
                        "where connection_request_id=?",
                connectionRequest.getCity(),
                connectionRequest.getAddress(),
                connectionRequest.getTariff().getTariffId(),
                connectionRequest.getDateOfChange(),
                connectionRequest.getStatus().name().toLowerCase(),
                connectionRequest.getConnectionRequestId()
        );
        return connectionRequest;
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

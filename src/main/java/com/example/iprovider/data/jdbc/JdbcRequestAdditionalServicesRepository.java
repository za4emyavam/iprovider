package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.RequestAdditionalServicesRepository;
import com.example.iprovider.entities.RequestAdditionalServices;
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
public class JdbcRequestAdditionalServicesRepository implements RequestAdditionalServicesRepository {
    private final JdbcTemplate jdbcTemplate;

    private final JdbcAdditionalServiceRepository jdbcAdditionalServiceRepository;

    private final JdbcConnectionRequestRepository jdbcConnectionRequestRepository;

    public JdbcRequestAdditionalServicesRepository(JdbcTemplate jdbcTemplate,
                                                   JdbcAdditionalServiceRepository jdbcAdditionalServiceRepository,
                                                   JdbcConnectionRequestRepository jdbcConnectionRequestRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAdditionalServiceRepository = jdbcAdditionalServiceRepository;
        this.jdbcConnectionRequestRepository = jdbcConnectionRequestRepository;
    }


    @Override
    public RequestAdditionalServices create(RequestAdditionalServices requestAdditionalServices) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into request_additional_services(request_id, services_id, status) " +
                        "VALUES (?, ?, ?::additional_service_status)",
                Types.INTEGER, Types.INTEGER, Types.VARCHAR
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        requestAdditionalServices.getRequestId().getConnectionRequestId(),
                        requestAdditionalServices.getServiceId().getAdditionalServiceId(),
                        requestAdditionalServices.getStatus()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        long requestAdditionalServiceId = Long.parseLong(Objects.requireNonNull(keyHolder.getKeys()).
                get("request_additional_services_id").toString());
        requestAdditionalServices.setRequestAdditionalServicesId(requestAdditionalServiceId);

        return requestAdditionalServices;
    }

    @Override
    public Optional<RequestAdditionalServices> read(Long requestAdditionalServicesId) {
        List<RequestAdditionalServices> results = jdbcTemplate.query(
                "select * from request_additional_services where request_additional_services_id = (?)",
                this::mapToRowRequestAdditionalServices,
                requestAdditionalServicesId);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Iterable<RequestAdditionalServices> readByConnectionRequestId(Long connectionRequestId) {
        return jdbcTemplate.query(
                "select * from request_additional_services where request_id = (?)",
                this::mapToRowRequestAdditionalServices,
                connectionRequestId
        );
    }

    @Override
    public Iterable<RequestAdditionalServices> readBySubscriberId(Long subscriberId) {
        return jdbcTemplate.query(
                "select ras.* from request_additional_services ras " +
                        "inner join connection_request cr " +
                        "on cr.connection_request_id = ras.request_id " +
                        "where cr.subscriber = (?)",
                this::mapToRowRequestAdditionalServices,
                subscriberId
        );
    }

    @Override
    public boolean delete(Long requestAdditionalServicesId) {
        return jdbcTemplate.update(
                "delete from request_additional_services where request_additional_services_id=?",
                requestAdditionalServicesId
        ) == 1;
    }

    private RequestAdditionalServices mapToRowRequestAdditionalServices(ResultSet row, int rowNum) throws SQLException {
        RequestAdditionalServices res = new RequestAdditionalServices();
        res.setRequestAdditionalServicesId(row.getLong("request_additional_services_id"));
        res.setRequestId(jdbcConnectionRequestRepository.read(row.getLong("request_id")).get());
        res.setServiceId(jdbcAdditionalServiceRepository.read(row.getLong("services_id")).get());
        res.setStatus(RequestAdditionalServices.Status.valueOf(row.getString("status")));
        return res;
    }
}

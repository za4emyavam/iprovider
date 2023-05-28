package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.ChecksRepository;
import com.example.iprovider.entities.Checks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcChecksRepository implements ChecksRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChecksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Checks> readAll(int page, int size) {
        int offset = (page - 1) * size;
        return jdbcTemplate.query(
                "select * from checks order by date_of_check desc limit ? offset ?",
                this::mapRowToChecks,
                size,
                offset
        );
    }

    @Override
    public Page<Checks> readAll(PageRequest pageRequest) {
        List<Checks> checks = jdbcTemplate.query(
                "select * from checks order by date_of_check desc limit ? offset ?",
                this::mapRowToChecks,
                pageRequest.getPageSize(),
                pageRequest.getOffset()
        );

        return new PageImpl<>(checks, pageRequest, getAmount());
    }

    @Override
    public Integer getAmount() {
        return jdbcTemplate.query(
                "select count(check_id) from checks",
                (rs, rowNum) -> rs.getInt(1)
        ).get(0);
    }

    @Override
    public Checks create(Long adminId) {
        jdbcTemplate.query("select * from f_check_payment(?)",
                (rs, rowNum) -> rs.getInt(1),
                adminId.intValue()
        );
        Optional<Checks> result = readLast();
        return result.get();
    }

    @Override
    public Optional<Checks> readLast() {
        List<Checks> results = jdbcTemplate.query(
                "SELECT * FROM checks c " +
                        "JOIN \"user\" u ON u.user_id = c.checker_id ORDER BY c.date_of_check DESC LIMIT 1",
                this::mapRowToChecks
        );
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    private Checks mapRowToChecks(ResultSet rs, int rowNum) throws SQLException {
        return new Checks(
                rs.getLong("check_id"),
                rs.getLong("checker_id"),
                rs.getInt("users"),
                rs.getDouble("amount"),
                rs.getDate("date_of_check")
        );
    }
}

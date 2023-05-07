package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.UserRepository;
import com.example.iprovider.entities.User;
import com.example.iprovider.entities.forms.SortUserStatisticsForm;
import com.example.iprovider.entities.forms.UserDetailsForm;
import org.springframework.jdbc.core.*;
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
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public User create(User user) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into \"user\" " +
                        "(email, pass, registration_date, user_role, user_status," +
                        " user_balance, firstname, surname, telephone_number) " +
                        "values (?, ?, ?, ?::role_type, ?::user_status_type, ?, ?, ?, ?) ",
                Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR,
                Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR
        );
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                user.getEmail(),
                                user.getPassword(),
                                user.getRegistrationDate(),
                                user.getUserRole().name().toLowerCase(),
                                user.getUserStatus().name().toLowerCase(),
                                user.getUserBalance(),
                                user.getFirstname(),
                                user.getSurname(),
                                user.getTelephoneNumber()
                        ));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        long userId = Long.parseLong(keyHolder.getKeys().get("user_id").toString());

        user.setUserId(userId);


        return user;
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = jdbcTemplate.query(
                "select * from \"user\" u where u.email=?", this::mapRowToUser, username
        );
        return users.size() == 0 ? null : users.get(0);
    }

    @Override
    public Optional<User> read(Long userId) {
        List<User> results = jdbcTemplate.query(
                "select user_id, email, registration_date, user_role," +
                        " user_status, user_balance, firstname, surname, telephone_number " +
                        "from \"user\" where user_id=?", this::mapRowToUserWithoutPass,
                userId
        );
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public Iterable<User> readAll() {
        return jdbcTemplate.query("select * from \"user\"", this::mapRowToUserWithoutPass);
    }

    @Override
    public Iterable<User> readAll(int page, int size) {
        int offset = (page - 1) * size;
        return jdbcTemplate.query(
                "select * from \"user\" order by user_id limit ? offset ?",
                this::mapRowToUserWithoutPass, size, offset);
    }

    @Override
    public Iterable<User> readAllHasTariffsWithSorting(SortUserStatisticsForm sortUserStatisticsForm) {
        if (sortUserStatisticsForm.getTariff() != 0 && !sortUserStatisticsForm.getAddress().isEmpty()) {
            return readAllWithSortByAddressAndTariff(sortUserStatisticsForm);
        } else if (sortUserStatisticsForm.getTariff() != 0 && sortUserStatisticsForm.getAddress().isEmpty()) {
            return readAllWithSortByTariff(sortUserStatisticsForm);
        } else if (sortUserStatisticsForm.getTariff() == 0 && !sortUserStatisticsForm.getAddress().isEmpty()) {
            return readAllWithSortByAddress(sortUserStatisticsForm);
        } else {
            return readAll(sortUserStatisticsForm);
        }
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(
                "update \"user\" u set email=?, user_role=?::role_type, " +
                        "user_status=?::user_status_type, telephone_number=? where user_id=?",
                user.getEmail(),
                user.getUserRole().name().toLowerCase(),
                user.getUserStatus().name().toLowerCase(),
                user.getTelephoneNumber(),
                user.getUserId()
        );
        return user;
    }

    @Override
    public User updatePass(User user) {
        jdbcTemplate.update(
                "update \"user\" u set pass=? where user_id=?",
                user.getPassword(),
                user.getUserId()
        );
        return user;
    }

    @Override
    public User updateDetails(UserDetailsForm userDetailsForm) {
        jdbcTemplate.update("update \"user\" u set email=?, firstname=?, surname=?, telephone_number=? " +
                "where u.user_id = ?",
                userDetailsForm.getEmail(),
                userDetailsForm.getFirstname(),
                userDetailsForm.getSurname(),
                userDetailsForm.getPhoneNumber(),
                userDetailsForm.getUserId()
        );
        return findByUsername(userDetailsForm.getEmail());
    }

    @Override
    public Integer getAmount() {
        return jdbcTemplate.query("select count(user_id) from \"user\"",
                        (rs, rowNum) -> rs.getInt(1))
                .get(0);
    }

    private User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
        return new User(
                row.getLong("user_id"),
                row.getString("email"),
                row.getString("pass"),
                row.getDate("registration_date"),
                User.RoleType.valueOf(row.getString("user_role").toUpperCase()),
                User.UserStatusType.valueOf(row.getString("user_status").toUpperCase()),
                row.getDouble("user_balance"),
                row.getString("firstname"),
                row.getString("surname"),
                row.getString("telephone_number")
        );
    }

    private User mapRowToUserWithoutPass(ResultSet row, int rowNum) throws SQLException {
        return new User(
                row.getLong("user_id"),
                row.getString("email"),
                row.getDate("registration_date"),
                User.RoleType.valueOf(row.getString("user_role").toUpperCase()),
                User.UserStatusType.valueOf(row.getString("user_status").toUpperCase()),
                row.getDouble("user_balance"),
                row.getString("firstname"),
                row.getString("surname"),
                row.getString("telephone_number")
        );
    }

    private Iterable<User> readAllWithSortByAddress(SortUserStatisticsForm sortUserStatisticsForm) {
        return jdbcTemplate.query("select distinct u.* from \"user\" u " +
                "inner join user_tariffs ut on u.user_id = ut.user_id " +
                "inner join connection_request cr on u.user_id = cr.subscriber " +
                "where ut.date_of_start >= ? " +
                "AND ut.date_of_start <= ? " +
                "AND cr.address LIKE ?",
                this::mapRowToUserWithoutPass,
                sortUserStatisticsForm.getDateOfStartPeriod(), sortUserStatisticsForm.getDateOfEndPeriod(),
                "%" + sortUserStatisticsForm.getAddress() + "%");
    }

    private Iterable<User> readAllWithSortByTariff(SortUserStatisticsForm sortUserStatisticsForm) {
        return jdbcTemplate.query("select distinct u.* from \"user\" u " +
                        "inner join user_tariffs ut on u.user_id = ut.user_id " +
                        "inner join connection_request cr on u.user_id = cr.subscriber " +
                        "where ut.date_of_start >= ? " +
                        "AND ut.date_of_start <= ? " +
                        "AND ut.tariff_id = ?",
                this::mapRowToUserWithoutPass,
                sortUserStatisticsForm.getDateOfStartPeriod(), sortUserStatisticsForm.getDateOfEndPeriod(),
                sortUserStatisticsForm.getTariff());
    }

    private Iterable<User> readAllWithSortByAddressAndTariff(SortUserStatisticsForm sortUserStatisticsForm) {
        return jdbcTemplate.query("select distinct u.* from \"user\" u " +
                        "inner join user_tariffs ut on u.user_id = ut.user_id " +
                        "inner join connection_request cr on u.user_id = cr.subscriber " +
                        "where ut.date_of_start >= ? " +
                        "AND ut.date_of_start <= ? " +
                        "AND cr.address LIKE ? " +
                        "AND ut.tariff_id = ?",
                this::mapRowToUserWithoutPass,
                sortUserStatisticsForm.getDateOfStartPeriod(), sortUserStatisticsForm.getDateOfEndPeriod(),
                "%" + sortUserStatisticsForm.getAddress() + "%", sortUserStatisticsForm.getTariff());
    }

    private Iterable<User> readAll(SortUserStatisticsForm sortUserStatisticsForm) {
        return jdbcTemplate.query("select distinct u.* from \"user\" u " +
                        "inner join user_tariffs ut on u.user_id = ut.user_id " +
                        "inner join connection_request cr on u.user_id = cr.subscriber " +
                        "where ut.date_of_start >= ? " +
                        "AND ut.date_of_start <= ? ",
                this::mapRowToUserWithoutPass,
                sortUserStatisticsForm.getDateOfStartPeriod(), sortUserStatisticsForm.getDateOfEndPeriod());
    }
}

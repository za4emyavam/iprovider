package com.example.iprovider.db;

import com.example.iprovider.data.UserRepository;
import com.example.iprovider.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    /*@Before
    public void setUp(){
        jdbcTemplate.execute("CREATE TABLE \"user\"" +
                "(" +
                "    user_id           SERIAL PRIMARY KEY," +
                "    email             varchar(320) NOT NULL UNIQUE," +
                "    pass              varchar(64)  NOT NULL," +
                "    registration_date date         NOT NULL DEFAULT (CURRENT_DATE)," +
                "    user_role         role_type             DEFAULT ('user')," +
                "    user_status       user_status_type      DEFAULT ('subscribed')," +
                "    user_balance      DECIMAL(8, 2)         DEFAULT 0 CHECK ( user_balance >= 0 )," +
                "    firstname         varchar(30)  NOT NULL," +
                "    middle_name       varchar(30)  NOT NULL," +
                "    surname           varchar(30)  NOT NULL," +
                "    telephone_number  varchar(30)  NOT NULL," +
                "    CONSTRAINT proper_email CHECK ( email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$' )," +
                "    CONSTRAINT proper_telephone_number CHECK (telephone_number ~ '^\\+38[0-9\\-\\+]{9,15}$' )" +
                ");");
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DROP TABLE \"user\"");
    }*/

    @Test
    public void findAll() {
        jdbcTemplate.update("INSERT INTO \"user\" (email, pass, registration_date, user_role, user_status, user_balance, firstname, surname, telephone_number)" +
                " VALUES ('example@gmail.com', 'WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=', CURRENT_DATE, DEFAULT, DEFAULT, DEFAULT,\n" +
                "        'Vasya', 'Pupkin'," +
                "        '+380634325657');");

        Iterable<User> entities = userRepository.readAll();
        List<User> users = new ArrayList<>();
        for (User user :
                entities) {
            users.add(user);
        }

        Assert.assertEquals(13, users.size());
    }

    @Test
    public void create() {
        Iterable<User> entities = userRepository.readAll();
        List<User> users = new ArrayList<>();
        for (User user :
                entities) {
            users.add(user);
        }
        Assert.assertEquals(12, users.size());

        jdbcTemplate.update("INSERT INTO \"user\" (email, pass, registration_date, user_role, user_status, user_balance, firstname, surname, telephone_number)" +
                " VALUES ('example@gmail.com', 'WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=', CURRENT_DATE, DEFAULT, DEFAULT, DEFAULT,\n" +
                "        'Vasya', 'Pupkin'," +
                "        '+380634325657');");

        entities = userRepository.readAll();
        users = new ArrayList<>();
        for (User user :
                entities) {
            users.add(user);
        }
        Assert.assertEquals(13, users.size());
    }

    @Test
    public void update() {
        User user = userRepository.read(12L).get();
        System.out.println(user);
        Assert.assertEquals("+380764325621", user.getTelephoneNumber());
        user.setTelephoneNumber("+380634325657");
        User updatedUser = userRepository.update(user);
        Assert.assertEquals("+380634325657", updatedUser.getTelephoneNumber());
    }
}

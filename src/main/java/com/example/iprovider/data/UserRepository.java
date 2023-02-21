package com.example.iprovider.data;

import com.example.iprovider.entities.User;
import com.example.iprovider.entities.forms.SortUserStatisticsForm;

import java.util.Optional;

public interface UserRepository {

    User create(User user);
    User findByUsername(String username);
    Optional<User> read(Long userId);
    Iterable<User> readAll();
    Iterable<User> readAll(int page, int size);
    Iterable<User> readAllHasTariffsWithSorting(SortUserStatisticsForm sortUserStatisticsForm);
    User update(User user);
    User updatePass(User user);
    Integer getAmount();
}

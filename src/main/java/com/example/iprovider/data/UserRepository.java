package com.example.iprovider.data;

import com.example.iprovider.entities.User;
import com.example.iprovider.entities.forms.SortUserStatisticsForm;
import com.example.iprovider.entities.forms.UserDetailsForm;

import java.util.Optional;

/**
 * Interface for working with user data.
 */
public interface UserRepository {
    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    User create(User user);

    /**
     * Find a user by username.
     *
     * @param username the username of the user to find
     * @return the user with the given username, or null if no such user exists
     */
    User findByUsername(String username);

    /**
     * Read a user by ID.
     *
     * @param userId the ID of the user to read
     * @return an Optional containing the requested user, or an empty Optional if no such user exists
     */
    Optional<User> read(Long userId);

    /**
     * Read all users.
     *
     * @return an Iterable containing all users, or an empty Iterable if no users were found
     */
    Iterable<User> readAll();

    /**
     * Read all users with pagination.
     *
     * @param page the page number to retrieve
     * @param size the number of users to retrieve per page
     * @return an Iterable containing the requested users, or an empty Iterable if no users were found
     */
    Iterable<User> readAll(int page, int size);

    /**
     * Read all users that have tariffs, sorted according to a specified form.
     *
     * @param sortUserStatisticsForm the form to use for sorting the users
     * @return an Iterable containing the requested users, or an empty Iterable if no users were found
     */
    Iterable<User> readAllHasTariffsWithSorting(SortUserStatisticsForm sortUserStatisticsForm);

    /**
     * Update a user.
     *
     * @param user the user to update
     * @return the updated user
     */
    User update(User user);

    /**
     * Update a user's password.
     *
     * @param user the user whose password to update
     * @return the updated user
     */
    User updatePass(User user);

    /**
     * Update a user's details.
     *
     * @param userDetailsForm the form containing the updated user details
     * @return the updated user
     */
    User updateDetails(UserDetailsForm userDetailsForm);

    /**
     * Get the total number of users.
     *
     * @return the number of users
     */
    Integer getAmount();
}

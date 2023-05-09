package com.example.iprovider.data;

import com.example.iprovider.entities.UserTariffs;

import java.util.Optional;

/**
 * This interface provides methods to work with user tariffs in the database.
 */
public interface UserTariffsRepository {
    Optional<UserTariffs> read(Long userTariffsId);
    /**
     * Retrieves all user tariffs by user ID.
     *
     * @param userId The ID of the user to retrieve tariffs for.
     * @return An iterable collection of user tariffs.
     */
    Iterable<UserTariffs> readById(Long userId);

    /**
     * Retrieves all user tariffs.
     *
     * @return An iterable collection of user tariffs.
     */
    Iterable<UserTariffs> readAll();

    /**
     * Retrieves all user tariffs with pagination.
     *
     * @param page The page number to retrieve.
     * @param size The number of user tariffs per page.
     * @return An iterable collection of user tariffs.
     */
    Iterable<UserTariffs> readAll(int page, int size);

    /**
     * Retrieves the total number of user tariffs.
     *
     * @return The total number of user tariffs.
     */
    Integer getAmount();

    /**
     * Retrieves the total number of subscribers to user tariffs.
     *
     * @return The total number of subscribers to user tariffs.
     */
    Integer getAmountOfSubscribers();

    /**
     * Deletes a user tariff by user ID and tariff ID.
     *
     * @param userId   The ID of the user.
     * @param tariffId The ID of the tariff.
     * @return True if the user tariff was deleted successfully, false otherwise.
     */
    boolean deleteByUserIdTariffId(Long userId, Long tariffId);
    boolean delete(Long userTariffId);
}

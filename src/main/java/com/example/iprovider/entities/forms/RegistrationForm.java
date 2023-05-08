package com.example.iprovider.entities.forms;

import com.example.iprovider.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;


/**
 * A form for user registration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$", message = "Invalid email format")
    private String username;
    @NotNull
    @Size(min=8, message = "Password must contain at least 8 characters")
    private String password;

    @NotNull
    private String confirmPass;
    @NotNull
    @NotEmpty(message = "Firstname can't be empty")
    private String firstname;
    @NotNull
    @NotEmpty(message = "Surname can't be empty")
    private String surname;
    @NotNull
    @Pattern(regexp = "^\\+38[0-9\\-\\+]{9,15}$", message = "Invalid phone number format")
    private String phone;

    /**
     * Converts the registration form to a new {@link User} object with encoded password, ready to be stored in the database.
     * @param passwordEncoder the {@link PasswordEncoder} used to encode the password
     * @return a new {@link User} object with the given attributes
     */

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                username, passwordEncoder.encode(password), new Date(), User.RoleType.USER,
                User.UserStatusType.SUBSCRIBED, 0.0,
                firstname, surname, phone
        );
    }
}

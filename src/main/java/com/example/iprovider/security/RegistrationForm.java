package com.example.iprovider.security;

import com.example.iprovider.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    private String username;
    private String password;
    private String firstname;
    private String surname;
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                username, passwordEncoder.encode(password), new Date(), User.RoleType.USER,
                User.UserStatusType.SUBSCRIBED, 0.0,
                firstname, surname, phone
        );
    }
}

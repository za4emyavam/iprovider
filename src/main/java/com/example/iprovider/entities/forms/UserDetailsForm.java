package com.example.iprovider.entities.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsForm {
    private Long userId;
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$", message = "Invalid email format")
    private String email;
    @NotNull
    @NotEmpty(message = "Firstname can't be empty")
    private String firstname;
    @NotNull
    @NotEmpty(message = "Surname can't be empty")
    private String surname;
    @NotNull
    @Pattern(regexp = "^\\+38[0-9\\-\\+]{9,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String password;

    public UserDetailsForm(String email, String firstname, String surname, String phoneNumber, String password) {
        this.email = email;
        this.firstname = firstname;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}

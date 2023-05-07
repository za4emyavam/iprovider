package com.example.iprovider.entities.forms;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsForm {
    private Long userId;
    @NotNull
    private String email;
    @NotNull
    private String firstname;
    @NotNull
    private String surname;
    @NotNull
    private String phoneNumber;

    public UserDetailsForm(String email, String firstname, String surname, String phoneNumber) {
        this.email = email;
        this.firstname = firstname;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }
}

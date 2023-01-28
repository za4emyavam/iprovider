package com.example.iprovider.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String email;
    private String password;
    private Date registrationDate;
    private RoleType userRole;
    private UserStatusType userStatus;
    private Double userBalance;
    private String firstname;
    private String surname;
    private String telephoneNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.userRole) {
            case MAIN_ADMIN -> Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"));
            case ADMIN -> Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
            default -> Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USER"));
        };
    }

    public User(String email, String password, Date registrationDate, RoleType userRole, UserStatusType userStatus, Double userBalance, String firstname, String surname, String telephoneNumber) {
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.userBalance = userBalance;
        this.firstname = firstname;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }

    public User(Long userId, String email, Date registrationDate, RoleType userRole, UserStatusType userStatus, Double userBalance, String firstname, String surname, String telephoneNumber) {
        this.userId = userId;
        this.email = email;
        this.registrationDate = registrationDate;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.userBalance = userBalance;
        this.firstname = firstname;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum RoleType {
        USER, ADMIN, MAIN_ADMIN
    }

    public enum UserStatusType {
        SUBSCRIBED, BLOCKED
    }
}

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

/**
 * The User class represents a user of the system. It implements the {@link UserDetails} interface from Spring Security,
 * which provides information about the user's security credentials and roles.
 */
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

    /**
     * Returns the authorities granted to the user. The authorities are based on the user's role in the system.
     * @return the authorities granted to the user.
     */
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

    /**
     * Returns the username used to authenticate the user.
     * @return the username used to authenticate the user.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Indicates whether the user's account has expired.
     * @return true if the user's account is valid (ie non-expired), false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * @return true if the user is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * @return true if the user's credentials are valid (ie non-expired), false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Represents the possible roles of a user in the system.
     */
    public enum RoleType {
        USER, ADMIN, MAIN_ADMIN
    }

    /**
     * Represents the possible status of a user in the system.
     */
    public enum UserStatusType {
        SUBSCRIBED, BLOCKED
    }
}

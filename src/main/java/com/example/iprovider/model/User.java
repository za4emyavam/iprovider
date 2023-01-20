package com.example.iprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
public class User implements UserDetails {
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

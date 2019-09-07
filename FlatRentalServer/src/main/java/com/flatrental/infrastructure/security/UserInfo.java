package com.flatrental.infrastructure.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flatrental.domain.user.User;
import com.flatrental.domain.userrole.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserInfo implements UserDetails {

    private Long id;

    private String name;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> userRoles;

    public UserInfo(Long id, String name, String username, String email, String password, Collection<? extends GrantedAuthority> userRoles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
    }

    public static UserInfo fromUser(User user) {
        List<GrantedAuthority> userRoles = user.getRoles()
                .stream()
                .map(UserInfo::mapToSimpleGrantedAuthority)
                .collect(Collectors.toList());

        return new UserInfo(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), userRoles);
    }

    private static SimpleGrantedAuthority mapToSimpleGrantedAuthority(UserRole userRole) {
        return new SimpleGrantedAuthority(userRole.getName().name());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserInfo)) {
            return false;
        }

        UserInfo that = (UserInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

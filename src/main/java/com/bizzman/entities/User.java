package com.bizzman.entities;

import com.bizzman.security.data.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "username", unique = true)
    @NotEmpty(message = "Username cannot be empty!")
    private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "password")
    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 8, max = 40, message = "Password length should be between 8 and 40 characters")
    private String password;

    private boolean enabled = true;

    @ElementCollection
    @Column(name = "authorities")
    private Set<Role> authorities = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(Role role) {
        authorities.add(role);
    }
}

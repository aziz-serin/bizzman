package com.bizzman.entities.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}

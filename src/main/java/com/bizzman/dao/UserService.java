package com.bizzman.dao;

import com.bizzman.dao.repos.UserRepository;
import com.bizzman.entities.user.Role;
import com.bizzman.entities.user.User;
import com.bizzman.entities.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

import java.util.Set;

import static java.lang.String.format;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                        format("User with username - %s, not found", username)));

        return UserDetailsImpl.build(user);
    }

    public User create(String username, String password, String rePassword, Role role) {
        if (usernameExists(username)) {
            throw new ValidationException("Username exists!");
        }
        if (!password.equals(rePassword)) {
            throw new ValidationException("Passwords do not match!");
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public long count() {
        return userRepository.count();
    }
}

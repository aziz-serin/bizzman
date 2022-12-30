package com.bizzman.dao;

import com.bizzman.dao.repos.UserRepository;
import com.bizzman.entities.User;
import com.bizzman.security.data.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                        format("User with username - %s, not found", username)));
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
        user.addAuthority(role);
        return userRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void updatePassword(String username, String newPassword, String reNewPassword) {
        if (!newPassword.equals(reNewPassword)) {
            throw new ValidationException("Passwords do not match!");
        }

        User user = loadUserByUsername(username);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));
        userRepository.deleteById(user.getId());
        Role role = (Role) user.getAuthorities().stream().collect(Collectors.toList()).get(0);
        User newUser = create(user.getUsername(), newPassword, reNewPassword, role);
        userRepository.save(newUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean userExistsById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public long count() {
        return userRepository.count();
    }
}

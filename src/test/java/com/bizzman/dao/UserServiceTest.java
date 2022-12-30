package com.bizzman.dao;

import com.bizzman.BizzmanApplication;
import com.bizzman.entities.User;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.validation.ValidationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    UserService userService;

    @Test
    public void loadByUsernameTestThrowsExceptionGivenUsernameDoesNotExist() {
       Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
           userService.loadUserByUsername("non-existing");
       });

       assertThat(exception).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void loadByUserNameSucceedsTest() {
        User user = userService.loadUserByUsername("admin");

        assertThat(user.getUsername()).isEqualTo("admin");
    }

    @Test
    public void usernameExistsTest() {
        assertThat(userService.usernameExists("admin")).isTrue();
    }

    @Test
    public void updatePasswordSucceedsTest() {
        User user = userService.loadUserByUsername("admin");
        String password = user.getPassword();
        userService.updatePassword(user.getUsername(), "newPassword", "newPassword");
        User updatedUser = userService.loadUserByUsername("admin");

        assertThat(password).isNotEqualTo(updatedUser.getPassword());
    }

    @Test
    public void updatePasswordThrowsExceptionGivenPasswordsDontMatch() {
        User user = userService.loadUserByUsername("admin");
        Exception exception = assertThrows(ValidationException.class, () -> {
            userService.updatePassword(user.getUsername(), "newPassword", "newPass");
        });

        assertThat(exception).isInstanceOf(ValidationException.class);
    }


    @Test
    public void userExistsByIdTest() {
        User user = userService.loadUserByUsername("admin");

        assertThat(userService.userExistsById(user.getId())).isTrue();
    }
}

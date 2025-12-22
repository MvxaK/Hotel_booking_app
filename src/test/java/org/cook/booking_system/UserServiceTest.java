package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.model.Role;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.UserCreateRequest;
import org.cook.booking_system.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    private static Long testId;

    @Test
    @Order(1)
    void createUser(){
        UserCreateRequest request = new UserCreateRequest("Immortal_NoName", "immortal_noname@gmail.com", "noname", Role.ROLE_USER);
        UserCreateRequest usernameExists = new UserCreateRequest("Immortal_NoName", "immortal@gmail.com", "noname", Role.ROLE_USER);
        UserCreateRequest emailExists = new UserCreateRequest("NoName", "immortal_noname@gmail.com", "noname", Role.ROLE_USER);

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(result.getUserName(), request.getUserName());
        assertEquals(result.getEmail(), request.getEmail());
        assertEquals(result.getRole(), request.getRole());

        testId = result.getId();

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(usernameExists);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(emailExists);
        });
    }

    @Test
    @Order(2)
    void getAllUsers(){
        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        for (User user: users){
            assertNotNull(user.getId());
            assertNotNull(user.getUserName());
            assertNotNull(user.getRole());
        }
    }

    @Test
    @Order(3)
    void getUserByUserName() {
        User user = userService.getUserByUserName("Immortal_NoName");

        assertEquals("Immortal_NoName", user.getUserName());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserByUserName("Unnamed");
        });
    }

    @Test
    @Order(4)
    void getUserById(){
        User result = userService.getUserById(testId);

        assertNotNull(result);
        assertEquals(testId, result.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(-999L);
        });
    }

    @Test
    @Order(5)
    void updateUserInfo(){
        String updateEmail = "Immortal_NoName@gmail.com";
        String currentPassword = "noname";

        User user = userService.updateUserInfo(testId, updateEmail, currentPassword);

        assertNotNull(user);
        assertEquals(updateEmail, user.getEmail());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserInfo(testId, updateEmail, "invalid_password");
        });
    }

    @Test
    @Order(6)
    void updateUserPassword(){
        String newPassword = "immortal_noname";
        String currentPassword = "noname";

        userService.updateUserPassword(testId, newPassword, currentPassword);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserPassword(testId, newPassword, currentPassword);
        });
    }

    @Test
    @Order(7)
    void deleteUser(){
        userService.deleteUser(testId);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(testId);
        });

        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(-999L);
        });
    }

}

package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.User;
import org.cook.booking_system.model.UserCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserService {

    User createUser(UserCreateRequest userCreateRequest);
    List<User> getAllUsers();
    User getUserByUserName(String username);
    User getUserById(Long id);
    User updateUserInfo(Long id, String newEmail, String password);
    void updateUserPassword(Long id, String password, String newPassword);
    void deleteUser(Long id);

}

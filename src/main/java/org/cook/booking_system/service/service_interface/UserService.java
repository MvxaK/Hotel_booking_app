package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.User;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    User createUser(RegisterRequest registerRequest);
    User getUserByUserName(String username);
    User getUserById(Long id);
    User updateUserInfo(Long id, User userToUpdate, String password);
    void updateUserPassword(Long id, String password, String newPassword);
    void deleteUser(Long id, String password);

}

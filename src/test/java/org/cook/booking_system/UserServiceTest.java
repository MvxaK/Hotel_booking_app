package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.RoleEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.mapper.UserMapper;
import org.cook.booking_system.model.Role;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.UserCreateRequest;
import org.cook.booking_system.repository.RoleRepository;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private User user;
    private final Long id = 42L;
    private final Long invalid_id = 67L;

    @BeforeEach
    void testData(){
        userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUserName("Carl");
        userEntity.setEmail("carl@gmail.com");
        userEntity.setPassword("carl_ciphered");
        userEntity.setRoomBookings(new ArrayList<>());
        userEntity.setHouseBookings(new ArrayList<>());

        user = new User();
        user.setId(id);
        user.setUserName("Carl");
        user.setEmail("carl@gmail.com");
        user.setRole(Role.ROLE_USER);
        user.setRoomBookingIds(new ArrayList<>());
        user.setHouseBookingIds(new ArrayList<>());
    }

    @Test
    void createUser(){
        UserCreateRequest request = new UserCreateRequest("Carl", "carl@gmail.com", "carl", Role.ROLE_USER);

        when(userRepository.findByUserName(request.getUserName())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("carl_ciphered");
        when(roleRepository.findByRole(request.getRole())).thenReturn(new RoleEntity());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toModel(any(UserEntity.class))).thenReturn(user);

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(result.getUserName(), request.getUserName());
        assertEquals(result.getEmail(), request.getEmail());
        assertEquals(result.getRole(), request.getRole());
        verify(userRepository).save(userEntity);

    }

    @Test
    void createUser_UserNameExistsError(){
        UserCreateRequest request = new UserCreateRequest("Carl", "carl@gmail.com", "carl", Role.ROLE_USER);

        when(userRepository.findByUserName(request.getUserName())).thenReturn(Optional.of(userEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(request);
        });

    }

    @Test
    void createUser_EmailExistsError(){
        UserCreateRequest request = new UserCreateRequest("Carl", "carl@gmail.com", "carl", Role.ROLE_USER);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(request);
        });

    }

    @Test
    void getAllUsers(){
        when(userRepository.findAll()).thenReturn(List.of(userEntity, userEntity));
        when(userMapper.toModel(any(UserEntity.class))).thenReturn(user);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUserByUserName() {
        when(userRepository.findByUserName("Carl")).thenReturn(Optional.of(userEntity));
        when(userMapper.toModel(userEntity)).thenReturn(user);

        User result = userService.getUserByUserName("Carl");

        assertEquals("Carl", result.getUserName());
    }

    @Test
    void getUserByUserName_NotFound() {
        when(userRepository.findByUserName("Unnamed")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserByUserName("Unnamed");
        });
    }

    @Test
    void getUserById(){
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userMapper.toModel(userEntity)).thenReturn(user);

        User result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getUserById_NotFound(){
        when(userRepository.findById(invalid_id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(invalid_id);
        });
    }

    @Test
    void updateUserInfo(){
        String updateEmail = "carl_johnson@gmail.com";
        String currentPassword = "carl";

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(currentPassword, userEntity.getPassword())).thenReturn(true);
        when(userRepository.existsByEmailAndIdNot(updateEmail, id)).thenReturn(false);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toModel(userEntity)).thenReturn(user);

        userService.updateUserInfo(id, updateEmail, currentPassword);

        assertEquals(updateEmail, userEntity.getEmail());
        verify(userRepository).save(userEntity);
    }

    @Test
    void updateUserInfo_InvalidPassword() {
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("invalid_password", userEntity.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserInfo(id, "carl_johnson@test.com", "invalid_password");
        });
    }

    @Test
    void updateUserPassword(){
        String newPassword = "carl_johnson";
        String currentPassword = "carl";

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(currentPassword, userEntity.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("new_carl_ciphered");

        userService.updateUserPassword(id, newPassword, currentPassword);

        assertEquals("new_carl_ciphered", userEntity.getPassword());
        verify(userRepository).save(userEntity);
    }

    @Test
    void updateUserPassword_InvalidPassword() {
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("invalid_password", userEntity.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserPassword(id, "carl_johnson", "invalid_password");
        });
    }

    @Test
    void deleteUser(){
        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUser_NotFound(){
        when(userRepository.existsById(invalid_id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(invalid_id);
        });
    }

}

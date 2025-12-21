package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.RoleEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.mapper.RoleMapper;
import org.cook.booking_system.mapper.UserMapper;
import org.cook.booking_system.model.Role;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.UserCreateRequest;
import org.cook.booking_system.repository.RoleRepository;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.service.service_interface.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User createUser(UserCreateRequest userCreateRequest){
        if(userRepository.findByUserName(userCreateRequest.getUserName()).isPresent()){
            throw new IllegalArgumentException("Username already exists");
        }

        if(userRepository.findByEmail(userCreateRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userCreateRequest.getUserName());
        userEntity.setEmail(userCreateRequest.getEmail());

        userEntity.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));

        RoleEntity userRole = roleRepository.findByRole(userCreateRequest.getRole());
        userEntity.setRoles(Set.of(userRole));

        return userMapper.toModel(userRepository.save(userEntity));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userRepository.findAll().stream()
                .map(userMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public User getUserByUserName(String username){
        return userRepository.findByUserName(username)
                .map(userMapper ::toModel)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username -> " + username));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));
    }

    @Transactional
    public User updateUserInfo(Long id, String newEmail, String currentPassword){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));

        if(!passwordEncoder.matches(currentPassword, userEntity.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }

        if (!newEmail.equals(userEntity.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(newEmail, id)) {
                throw new IllegalArgumentException("This email already exists");
            }
            userEntity.setEmail(newEmail);
        }

        return userMapper.toModel(userRepository.save(userEntity));
    }

    @Transactional
    public void updateUserPassword(Long id, String newPassword, String currentPassword){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));

        if(!passwordEncoder.matches(currentPassword, userEntity.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(userEntity);
    }

    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User not found with id -> " + id);
        }

        userRepository.deleteById(id);
    }
}

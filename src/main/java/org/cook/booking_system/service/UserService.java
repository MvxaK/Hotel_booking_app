package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.RoleEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.mapper.UserMapper;
import org.cook.booking_system.model.Role;
import org.cook.booking_system.model.User;
import org.cook.booking_system.repository.RoleRepository;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Transactional
    public User createUser(RegisterRequest registerRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(registerRequest.getUsername());

        if(emailCheck(registerRequest.getEmail())){
            userEntity.setEmail(registerRequest.getEmail());
        }else
            throw new IllegalArgumentException("This is email already in use");

        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        RoleEntity userRole = roleRepository.findByRole(Role.ROLE_USER);
        userEntity.setRoles(Set.of(userRole));

        logger.info("User with id = {} is created ", userEntity.getId());
        return userMapper.toModel(userRepository.save(userEntity));
    }

    @Transactional(readOnly = true)
    public User getUserName(String username){
        return userRepository.findByUserName(username)
                .map(userMapper ::toModel)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username -> " + username));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id){
        return userRepository.findById(id)
                .map(userMapper ::toModel)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));
    }

    @Transactional
    public User updateUserInfo(Long id, User userToUpdate, String password){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));

        if(passwordCheck(id, password)){
           throw new IllegalArgumentException("Invalid password");
        }

        if(!emailUpdateCheck(id, userToUpdate.getEmail())){
            throw new IllegalArgumentException("This email already exists");
        }

        userEntity.setUserName(userToUpdate.getUserName());
        userEntity.setEmail(userToUpdate.getEmail());

        logger.info("User with id = {} is updated", id);
        return userMapper.toModel(userRepository.save(userEntity));
    }

    @Transactional
    public void updateUserPassword(Long id, String password, String newPassword){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));

        if(passwordCheck(id, password)){
            throw new IllegalArgumentException("Invalid password");
        }

        logger.info("User with id = {} updated password", id);
        userEntity.setPassword(newPassword);
    }

    @Transactional
    public void deleteUser(Long id, String password){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User not found with id -> " + id);
        }

        if(passwordCheck(id, password)){
            throw new IllegalArgumentException("Invalid password");
        }

        logger.info("User with id = {} is deleted", id);
        userRepository.deleteById(id);
    }

    public Boolean emailUpdateCheck(Long id, String email){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id -> " + id));

        UserEntity userByEmail = userRepository.findByEmail(email)
                .orElse(null);

        if(user.getId().equals(userByEmail != null ? userByEmail.getId() : null))
            return true;
        else if(userByEmail == null)
            return true;
        else
            return false;
    }

    public Boolean emailCheck(String email){
        UserEntity userByEmail = userRepository.findByEmail(email)
                .orElse(null);

        return userByEmail == null;
    }

    public boolean passwordCheck(Long id, String password){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id -> " + id));

        return userEntity.getPassword().equals(passwordEncoder.encode(password));
    }
}

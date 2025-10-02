package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.mapper.UserMapper;
import org.cook.booking_system.model.User;
import org.cook.booking_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public User createUser(User user){
        UserEntity userEntity = userMapper.toUserEntity(user);
        logger.info("User with id = {} is created ", user.getId());
        return userMapper.toUserModel(userRepository.save(userEntity));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id){
        return userRepository.findById(id)
                .map(userMapper :: toUserModel)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));
    }

    @Transactional
    public User updatePassword(Long id, User userToUpdate, String password, String newPassword){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + id));

        if(!userEntity.getPassword().equals(password)){
           throw new IllegalArgumentException("Invalid password");
        }

        userEntity.setUserName(userToUpdate.getUsername());
        userEntity.setEmail(userToUpdate.getEmail());
        userEntity.setPassword(newPassword);

        logger.info("User with id = {} is updated", id);
        return userMapper.toUserModel(userRepository.save(userEntity));
    }

    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User not found with id -> " + id);
        }

        logger.info("User with id = {} is deleted", id);
        userRepository.deleteById(id);
    }
}

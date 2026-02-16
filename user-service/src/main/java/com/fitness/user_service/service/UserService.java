package com.fitness.user_service.service;

import com.fitness.user_service.dto.RegisterRequest;
import com.fitness.user_service.dto.UserResponse;
import com.fitness.user_service.exception.EmailAlreadyExistException;
import com.fitness.user_service.exception.UserNotFoundException;
import com.fitness.user_service.model.User;
import com.fitness.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    UserRepository userRepository;
    private static final Logger log= LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserProfile(String userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistException("Email Exist Already");
        }


        User user= User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        /* saved User in db*/
        User savedUser=userRepository.save(user);
        /* return UseResponse*/
        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }
}

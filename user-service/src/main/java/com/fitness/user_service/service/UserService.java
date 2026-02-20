package com.fitness.user_service.service;

import com.fitness.user_service.dto.RegisterRequest;
import com.fitness.user_service.dto.UserResponse;
import com.fitness.user_service.exception.EmailAlreadyExistException;
import com.fitness.user_service.exception.UserNotFoundException;
import com.fitness.user_service.model.User;
import com.fitness.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserProfile(String userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .keyCloakId(user.getKeyCloakId())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            User existingUser=userRepository.findByEmail(request.getEmail());
            /* return UseResponse*/
            return UserResponse.builder()
                               .id(existingUser.getId())
                               .keyCloakId(existingUser.getKeyCloakId())
                               .email(existingUser.getEmail())
                               .password(existingUser.getPassword())
                               .firstName(existingUser.getFirstName())
                               .lastName(existingUser.getLastName())
                               .createdAt(existingUser.getCreatedAt())
                               .updatedAt(existingUser.getUpdatedAt())
                               .build();
        }


        User user= User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .keyCloakId(request.getKeyCloakId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        /* saved User in db*/
        User savedUser=userRepository.save(user);
        /* return UseResponse*/
        return UserResponse.builder()
                .id(savedUser.getId())
                .keyCloakId(savedUser.getKeyCloakId())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }

    public Boolean validateUserId(String userId) {
        log.info("Calling UserId Validation Method:: userId : {}", userId);
        return userRepository.existsById(userId);
    }

	public Boolean existByKeyCloakId(String keyCloakId) {
        return userRepository.existsByKeyCloakId(keyCloakId);
	}
}

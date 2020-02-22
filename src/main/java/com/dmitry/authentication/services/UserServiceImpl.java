package com.dmitry.authentication.services;

import com.dmitry.authentication.configurations.ApplicationConfiguration;
import com.dmitry.authentication.models.dto.UserDTO;
import com.dmitry.authentication.models.entities.User;
import com.dmitry.authentication.passwordhashing.PBKDF2Hasher;
import com.dmitry.authentication.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationConfiguration applicationConfiguration;
    private final PBKDF2Hasher hasher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ApplicationConfiguration applicationConfiguration, PBKDF2Hasher hasher) {
        this.userRepository = userRepository;
        this.applicationConfiguration = applicationConfiguration;
        this.hasher = hasher;
    }

    @Override
    public String registration(UserDTO userDTO) {
        String response = "";
        User user = applicationConfiguration.modelMapper().map(userDTO, User.class);
        user.setRegistrationDate(Instant.now().toEpochMilli());
        user.setPassword(hasher.hash(userDTO.getPassword().toCharArray()));
        User registeredUser = userRepository.save(user);
        response = registeredUser.getFirstName() + " " + registeredUser.getLastName() + " was registered successfully.";
        log.info(response);
        return response;
    }

    @Override
    public String logIn(String email, String password) {
        User user = userRepository.findByEmail(email);
        String logInResponse = "";
        if (user == null){
            logInResponse = "User with giving user name not found";
            log.warn(logInResponse);
        } else {
            UserDTO userDTO = applicationConfiguration.modelMapper().map(user, UserDTO.class);
            String token = userDTO.getPassword();
            if (!hasher.checkPassword(password.toCharArray(), token)) {
                logInResponse = "Password doesn't match the entered";
                log.warn(logInResponse);
            } else {
                logInResponse = token;
            }
        }
        return logInResponse;
    }

    @Override
    public String getFullName(String token) {
        User user = userRepository.findByPassword(token);
        String response = "";
        if (user == null){
            response = "User with giving token not found";
            log.warn(response);
        } else {
            UserDTO userDTO = applicationConfiguration.modelMapper().map(user, UserDTO.class);
            response = userDTO.getFirstName() + " " + userDTO.getLastName() + " was found.";
            log.info(response);
        }
        return response;
    }
}

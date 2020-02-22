package com.dmitry.authentication.services;

import com.dmitry.authentication.models.dto.UserDTO;

public interface UserService {

    String registration(UserDTO userDTO);
    String logIn(String email, String password);
    String getFullName (String token);
}

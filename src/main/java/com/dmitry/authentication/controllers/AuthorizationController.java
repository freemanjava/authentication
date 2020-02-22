package com.dmitry.authentication.controllers;

import com.dmitry.authentication.models.dto.UserDTO;
import com.dmitry.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private final UserService userService;

    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authorization")
    public String authorization(@RequestBody Map<String, String> authParam) {
        String email = authParam.get("email");
        String password = authParam.get("password");
        return userService.logIn(email, password);
    }

    @PostMapping("/user")
    public String getFullName(@RequestBody Map<String, String> token) {
        String password = token.get("password");
        return userService.getFullName(password);
    }

    @PutMapping("/registration")
    public String registration(@RequestBody UserDTO userDTO) {
        return userService.registration(userDTO);
    }
}

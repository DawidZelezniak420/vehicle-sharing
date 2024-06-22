package com.car.sharing.zelezniak.userdomain.controller;

import com.car.sharing.zelezniak.userdomain.model.user.Client;
import com.car.sharing.zelezniak.userdomain.model.login.LoginRequest;
import com.car.sharing.zelezniak.userdomain.model.login.LoginResponse;
import com.car.sharing.zelezniak.userdomain.service.authentication.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final Authenticator authentication;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Client register(
            @RequestBody Client client){
    return authentication.register(client);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        System.out.println("Login request: " + loginRequest);
        LoginResponse loginResponse = authentication.login(loginRequest);
        System.out.println("Login response: " + loginResponse);
        return loginResponse;
    }



}

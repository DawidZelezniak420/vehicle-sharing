package com.car.sharing.zelezniak.user_domain.controller;

import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.model.login.LoginRequest;
import com.car.sharing.zelezniak.user_domain.model.login.LoginResponse;
import com.car.sharing.zelezniak.user_domain.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Client register(
            @RequestBody Client client){
    return authService.register(client);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoginResponse login(
            @RequestBody LoginRequest loginRequest){
       return authService.login(loginRequest);
    }



}

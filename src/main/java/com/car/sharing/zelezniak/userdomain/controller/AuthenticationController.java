package com.car.sharing.zelezniak.userdomain.controller;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
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
    public ApplicationUser register(
            @RequestBody ApplicationUser user){
    return authentication.register(user);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest loginRequest){
        return authentication.login(loginRequest);
    }



}

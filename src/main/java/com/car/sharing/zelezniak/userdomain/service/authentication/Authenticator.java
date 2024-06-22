package com.car.sharing.zelezniak.userdomain.service.authentication;

import com.car.sharing.zelezniak.userdomain.model.login.LoginRequest;
import com.car.sharing.zelezniak.userdomain.model.login.LoginResponse;
import com.car.sharing.zelezniak.userdomain.model.user.Client;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface Authenticator extends UserDetailsService {

    Client register(Client applicationUser);

    LoginResponse login(LoginRequest loginRequest);
}

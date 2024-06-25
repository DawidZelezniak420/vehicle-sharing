package com.car.sharing.zelezniak.user_domain.service.authentication;

import com.car.sharing.zelezniak.user_domain.model.login.LoginRequest;
import com.car.sharing.zelezniak.user_domain.model.login.LoginResponse;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface Authenticator extends UserDetailsService {

    Client register(Client applicationUser);

    LoginResponse login(LoginRequest loginRequest);
}

package com.car.sharing.zelezniak.user_domain.service.authentication;

import com.car.sharing.zelezniak.user_domain.model.login.LoginRequest;
import com.car.sharing.zelezniak.user_domain.model.login.LoginResponse;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.model.user.Role;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.car.sharing.zelezniak.user_domain.repository.RoleRepository;
import com.car.sharing.zelezniak.user_domain.service.UserValidator;
import com.car.sharing.zelezniak.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationService implements Authenticator {

    private final ClientRepository clientRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserValidator validator;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {
        validator.throwExceptionIfObjectIsNull(username);
        return clientRepository.findByCredentialsEmail(username);
    }

    public Client register(
            Client client) {
        validator.throwExceptionIfObjectIsNull(client);
        validator.ifUserExistsThrowException(client.getEmail());
        add(client);
        return client;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return tryLoginUser(email, password);
    }

    private void add(Client client) {
        setRequiredDataAndEncodePassword(client);
        clientRepository.save(client);
    }

    private void setRequiredDataAndEncodePassword(
            Client client) {
        client.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        client.setCredentials(
                new UserCredentials(client.getEmail(),
                        encoder.encode(client.getPassword())));
        handleAddRoleForUser(client);
    }

    private void handleAddRoleForUser(
            Client client) {
        Role roleUser = findOrCreateRoleUser();
        client.addRole(roleUser);
    }

    private Role findOrCreateRoleUser() {
        Role role = roleRepository.findByRoleName("USER");
        if (role == null) {
            role = new Role("USER");
            roleRepository.save(role);
        }
        return role;
    }

    private LoginResponse tryLoginUser(String email
            , String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, password));
            String token = jwtGenerator.generateJWT(auth);
            return new LoginResponse(
                    clientRepository.findByCredentialsEmail(email),
                    token);
        } catch (AuthenticationException e) {
            log.error("User with email : " + email + " can not be authenticated - bad credentials.");
            return new LoginResponse(null, "");
        }
    }

}

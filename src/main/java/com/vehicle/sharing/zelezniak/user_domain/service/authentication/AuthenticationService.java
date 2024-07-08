package com.vehicle.sharing.zelezniak.user_domain.service.authentication;

import com.vehicle.sharing.zelezniak.user_domain.model.login.*;
import com.vehicle.sharing.zelezniak.user_domain.model.client.*;
import com.vehicle.sharing.zelezniak.user_domain.model.client.value_objects.UserCredentials;
import com.vehicle.sharing.zelezniak.user_domain.repository.*;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientValidator;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import com.vehicle.sharing.zelezniak.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final ClientValidator clientValidator;
    private final InputValidator inputValidator;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {
        inputValidator.throwExceptionIfObjectIsNull(
                username, "Email can not be a null.");
        return clientRepository.findByCredentialsEmail(username);
    }

    public Client register(
            Client client) {
        validateData(client);
        saveClient(client);
        return client;
    }

    private void validateData(Client client) {
        inputValidator.throwExceptionIfObjectIsNull(
                client, InputValidator.CLIENT_NOT_NULL);
        EmailPatternValidator.validate(client.getEmail());
        clientValidator.ifUserExistsThrowException(
                client.getEmail());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return tryLoginUser(email, password);
    }

    private void saveClient(Client client) {
        setRequiredDataAndEncodePassword(client);
        clientRepository.save(client);
    }

    private void setRequiredDataAndEncodePassword(
            Client client) {
        client.setCreatedAt(
                TimeFormatter.getFormattedActualDateTime());
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
        Role role = roleRepository.findByRoleName(
                "USER");
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
            log.error("User with email : " + email +
                    " can not be authenticated - bad credentials.");
            return new LoginResponse(null, "");
        }
    }

}

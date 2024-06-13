package com.car.sharing.zelezniak.userdomain.service.authentication;

import com.car.sharing.zelezniak.userdomain.model.login.LoginRequest;
import com.car.sharing.zelezniak.userdomain.model.login.LoginResponse;
import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.user.Role;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import com.car.sharing.zelezniak.userdomain.repository.RoleRepository;
import com.car.sharing.zelezniak.userdomain.service.UserValidator;
import jakarta.persistence.EntityManager;
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

    private final AppUserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserValidator validator;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final EntityManager entityManager;

    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {
        validator.throwExceptionIfObjectIsNull(username);
        return userRepository.findByCredentialsEmail(username);
    }

    public ApplicationUser register(
            ApplicationUser newUser) {
        validator.throwExceptionIfObjectIsNull(newUser);
        validator.ifUserExistsThrowException(newUser.getEmail());
        add(newUser);
        return newUser;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return tryLoginUser(email, password);
    }

    private void add(ApplicationUser user) {
        setRequiredDataAndEncodePassword(user);
        userRepository.save(user);
    }

    private void setRequiredDataAndEncodePassword(
            ApplicationUser user) {
        user.setCreationDate();
        user.setCredentials(
                new UserCredentials(user.getEmail(),
                        encoder.encode(user.getPassword())));
        handleAddRoleForUser(user);
    }

    private void handleAddRoleForUser(
            ApplicationUser appUser) {
        Role roleUser = findOrCreateRoleUser();
        appUser.addRole(roleUser);
    }

    private Role findOrCreateRoleUser() {
        Role role = roleRepository.findByRoleName("USER");
        if (role == null) {
            role = new Role("USER");
            entityManager.persist(role);
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
                    userRepository.findByCredentialsEmail(email), token);
        } catch (AuthenticationException e) {
            log.error("User with email : " + email + " can not be authenticated - bad credentials.");
            return new LoginResponse(null, "");
        }
    }

}

package com.car.sharing.zelezniak.userdomain.service;

import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import com.car.sharing.zelezniak.userdomain.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserOperations implements UserService {

    private final AppUserRepository appUserRepository;
    private final UserDAO userDAO;
    private final UserValidator validator;

    @Transactional(readOnly = true)
    public List<ApplicationUser> getAll() {
        return appUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ApplicationUser findById(Long id) {
        validator.throwExceptionIfObjectIsNull(id);
        return findUser(id);
    }


    public void add(ApplicationUser appUser) {
        validator.throwExceptionIfObjectIsNull(appUser);
        addUser(appUser);
    }

    private ApplicationUser findUser(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "User with id : " + id + " does not exists."));
    }

    private void addUser(ApplicationUser appUser) {
        appUserRepository.save(appUser);
    }
}

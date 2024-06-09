package com.car.sharing.zelezniak.userdomain.service;

import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import com.car.sharing.zelezniak.userdomain.repository.UserDAO;
import com.car.sharing.zelezniak.utils.TimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserOperations implements UserService {

    private final AppUserRepository userRepository;
    private final UserDAO userDAO;
    private final UserOperationsValidator validator;

    @Transactional(readOnly = true)
    public List<ApplicationUser> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ApplicationUser findById(Long id) {
        validator.throwExceptionIfObjectIsNull(id);
        return findUser(id);
    }

    @Transactional
    public void add(ApplicationUser appUser) {
        validator.throwExceptionIfObjectIsNull(appUser);
        validator.checkIfUserExists(appUser.getEmail());
        addUser(appUser);
    }

    @Transactional
    public void update(Long id, ApplicationUser newData) {
        validator.throwExceptionIfObjectIsNull(id);
        validator.throwExceptionIfObjectIsNull(newData);
        ApplicationUser userFromDb = findUser(id);
        String userFromDbEmail = userFromDb.getEmail();
        validator.checkIfUserCanBeUpdated(userFromDbEmail, newData);
        updateUser(userFromDb,newData);
        userRepository.save(userFromDb);
    }

    private void updateUser(ApplicationUser userFromDb, ApplicationUser newData) {
        userFromDb.setName(newData.getName());
        userFromDb.setCredentials(newData.getCredentials());
        userFromDb.setAddress(newData.getAddress());

    }

    private ApplicationUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "User with id : " + id + " does not exists."));
    }

    private void addUser(ApplicationUser appUser) {
        var creationDate =
                TimeFormatter.getFormattedActualDateTime();
        appUser.setCreatedAt(creationDate);
        userRepository.save(appUser);
    }


}

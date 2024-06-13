package com.car.sharing.zelezniak.userdomain.service;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserOperations implements UserService {

    private final AppUserRepository userRepository;
    private final UserValidator validator;

    @Transactional(readOnly = true)
    public List<ApplicationUser> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ApplicationUser getById(Long id) {
        validator.throwExceptionIfObjectIsNull(id);
        return findUser(id);
    }

    @Transactional
    public ApplicationUser update(Long id,
                                  ApplicationUser newData) {
        validator.throwExceptionIfObjectIsNull(id);
        validator.throwExceptionIfObjectIsNull(newData);
        ApplicationUser userFromDb = findUser(id);
        return validateAndUpdateUser(userFromDb, newData);
    }

    @Transactional
    public void delete(Long id) {
        validator.throwExceptionIfObjectIsNull(id);
        ApplicationUser userToDelete = findUser(id);
        handleDeleteUser(userToDelete);
    }

    private ApplicationUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "User with id : " + id + " does not exists."));
    }

    private ApplicationUser validateAndUpdateUser(ApplicationUser userFromDb,
                                                  ApplicationUser newData) {
        String userFromDbEmail = userFromDb.getEmail();
        validator.checkIfUserCanBeUpdated(userFromDbEmail, newData);
        userFromDb.setName(newData.getName());
        userFromDb.setCredentials(newData.getCredentials());
        userFromDb.setAddress(newData.getAddress());
        return userFromDb;
    }

    private void handleDeleteUser(ApplicationUser userToDelete) {
        removeRoles(userToDelete);
        userRepository.delete(userToDelete);
    }

    private void removeRoles(ApplicationUser userToDelete) {
        userToDelete.setRoles(null);
    }
}

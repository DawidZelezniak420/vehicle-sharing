package com.car.sharing.zelezniak.userdomain.service;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final AppUserRepository userRepository;

    public void throwExceptionIfObjectIsNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException(
                    "Argument is incorrect");
        }
    }

    public void ifUserExistsThrowException(String email) {
        if (userWithSuchEmailExists(email))
            throw new IllegalArgumentException(
                   createMessage(email));
    }

    public void checkIfUserCanBeUpdated(String userFromDbEmail,
                                        ApplicationUser newData) {
        String newEmail = newData.getEmail();
        if(emailsAreNotSame(userFromDbEmail,newEmail)
        && userWithSuchEmailExists(newEmail)){
            throw new IllegalArgumentException(
                    createMessage(newEmail));
        }
    }

    private boolean emailsAreNotSame(String userFromDbEmail,
                                     String newEmail) {
        return !userFromDbEmail.equals(newEmail);
    }

    private boolean userWithSuchEmailExists(String newEmail) {
       return userRepository.existsByCredentialsEmail(newEmail);
    }

    private String createMessage(String email) {
        return  "User with email : " + email + " already exists";
    }
}

package com.car.sharing.zelezniak.userdomain.service;

import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import com.car.sharing.zelezniak.userdomain.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserOperations implements UserService {

    private final AppUserRepository appUserRepository;
    private final UserDAO userDAO;

    @Transactional(readOnly = true)
    public Collection<ApplicationUser> getAll(){
    return appUserRepository.findAll();
    }
}

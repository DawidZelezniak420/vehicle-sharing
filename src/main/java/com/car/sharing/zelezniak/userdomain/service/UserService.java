package com.car.sharing.zelezniak.userdomain.service;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;

import java.util.Collection;

public interface UserService {

    Collection<ApplicationUser> getAll();

    ApplicationUser getById(Long id);

    ApplicationUser update(Long id, ApplicationUser newData);

    void delete(Long id);
}

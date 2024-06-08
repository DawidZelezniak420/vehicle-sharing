package com.car.sharing.zelezniak.userdomain.repository;

import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<ApplicationUser,Long> {
}

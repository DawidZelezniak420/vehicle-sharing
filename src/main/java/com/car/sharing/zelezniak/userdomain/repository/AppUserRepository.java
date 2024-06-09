package com.car.sharing.zelezniak.userdomain.repository;

import com.car.sharing.zelezniak.userdomain.model.Address;
import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<ApplicationUser,Long> {

    boolean existsByCredentialsEmail(String email);
    long countByAddress(Address address);

}

package com.car.sharing.zelezniak.userdomain.repository;

import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppUserRepository extends JpaRepository<ApplicationUser, Long> {

    boolean existsByCredentialsEmail(String email);

    ApplicationUser findByCredentialsEmail(String email);

    @Modifying
    @Query("UPDATE ApplicationUser u SET u.name = :username , u.credentials = :credentials , u.address = :address WHERE" +
            " u.id =:id")
    void updateUser(@Param("id") Long id,
                    @Param("username") UserName username,
                    @Param("credentials") UserCredentials credentials,
                    @Param("address") Address address);

}

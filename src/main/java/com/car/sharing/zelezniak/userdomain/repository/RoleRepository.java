package com.car.sharing.zelezniak.userdomain.repository;

import com.car.sharing.zelezniak.userdomain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}

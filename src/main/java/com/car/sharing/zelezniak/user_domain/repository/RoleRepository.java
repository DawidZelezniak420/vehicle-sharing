package com.car.sharing.zelezniak.user_domain.repository;

import com.car.sharing.zelezniak.user_domain.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(String user);
}

package com.car.sharing.zelezniak.userdomain.repository;

import com.car.sharing.zelezniak.userdomain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}

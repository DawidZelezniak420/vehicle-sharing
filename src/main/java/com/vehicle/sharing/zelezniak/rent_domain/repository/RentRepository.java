package com.vehicle.sharing.zelezniak.rent_domain.repository;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent,Long> {
}

package com.vehicle.rental.zelezniak.rent_domain.repository;

import com.vehicle.rental.zelezniak.rent_domain.model.Rent;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface RentRepository extends JpaRepository<Rent, Long> {

    @Query("SELECT r FROM Rent r WHERE r.client.id = :id")
    Page<Rent> findAllByClientId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT v FROM Rent r JOIN r.vehicles v WHERE r.id = :id")
    Page<Vehicle> findVehiclesByRentId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT v.id FROM Rent r " +
            "JOIN r.vehicles v " +
            "WHERE r.rentInformation.rentDuration.rentalStart <= :end " +
            "AND r.rentInformation.rentDuration.rentalEnd >= :start " +
            "AND r.rentStatus = 'ACTIVE'")
    Set<Long> unavailableVehicleIdsForRentInPeriod(
            @Param("start") LocalDateTime start,
            @Param("end")LocalDateTime end);
}

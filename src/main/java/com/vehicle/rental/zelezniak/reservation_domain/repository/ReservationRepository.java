package com.vehicle.rental.zelezniak.reservation_domain.repository;

import com.vehicle.rental.zelezniak.rent_domain.model.Rent;
import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :id")
    Collection<Rent> findAllByClientId(@Param("id") Long id);

    @Query("SELECT v FROM Reservation r JOIN r.vehicles v WHERE r.id = :id")
    Collection<Vehicle> findVehiclesByReservationId(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r JOIN r.vehicles v WHERE r.reservationStatus = 'ACTIVE' AND v.id = :id")
    Collection<Reservation> findActiveReservationsByVehicleId(@Param("id") Long id);

    @Query("SELECT v.id FROM Reservation r " +
            "JOIN r.vehicles v " +
            "WHERE r.rentInformation.rentDuration.rentalStart <= :end " +
            "AND r.rentInformation.rentDuration.rentalEnd >= :start " +
            "AND r.reservationStatus = 'ACTIVE'")
    Set<Long> unavailableVehicleIdsForReservationInPeriod(
            @Param("start") LocalDateTime start,
            @Param("end")LocalDateTime end);
}

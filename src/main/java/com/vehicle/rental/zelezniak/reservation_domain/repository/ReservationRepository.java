package com.vehicle.rental.zelezniak.reservation_domain.repository;

import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :id")
    Collection<Reservation> findAllByClientId(Long id);

    @Query("SELECT v FROM Reservation r JOIN r.vehicles v WHERE r.id = :id")
    Collection<Vehicle> findVehiclesByReservationId(Long id);

    @Query("SELECT v.id FROM Reservation r " +
            "JOIN r.vehicles v " +
            "WHERE r.rentInformation.rentDuration.rentalStart <= :end " +
            "AND r.rentInformation.rentDuration.rentalEnd >= :start " +
            "AND r.reservationStatus = 'ACTIVE'")
    Set<Long> unavailableVehicleIdsForReservationInPeriod(
            LocalDateTime start,
            LocalDateTime end);

    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM reserved_vehicles " +
            "WHERE reservation_id = :reservationId AND vehicle_id = :vehicleId")
    void deleteVehicleFromReservation(
            Long reservationId,
            Long vehicleId);
}

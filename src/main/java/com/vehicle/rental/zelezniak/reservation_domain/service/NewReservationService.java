package com.vehicle.rental.zelezniak.reservation_domain.service;

import com.vehicle.rental.zelezniak.common_value_objects.RentDuration;
import com.vehicle.rental.zelezniak.common_value_objects.RentInformation;
import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.reservation_domain.model.util.ReservationCreationRequest;
import com.vehicle.rental.zelezniak.reservation_domain.repository.ReservationRepository;
import com.vehicle.rental.zelezniak.reservation_domain.service.reservation_update.ReservationUpdateStrategy;
import com.vehicle.rental.zelezniak.reservation_domain.service.reservation_update.ReservationUpdateStrategyFactory;
import com.vehicle.rental.zelezniak.user_domain.model.client.Client;
import com.vehicle.rental.zelezniak.user_domain.service.ClientService;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 *  Service class responsible for managing reservations that are in the NEW status.
 */
@Service
@RequiredArgsConstructor
public class NewReservationService {

    private final ClientService clientService;
    private final ReservationCostCalculator calculator;
    private final ReservationUpdateStrategyFactory strategyFactory;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation addNewReservation(ReservationCreationRequest request) {
        Client client = clientService.findById(request.getClientId());
        return buildAndSaveReservation(client, request.getDuration());
    }

    @Transactional
    public Reservation updateReservation(Reservation existing, Reservation newData) {
        checkIfStatusIsEqualNEW(existing, "Can not update reservation with status: "
                + existing.getReservationStatus());

        ReservationUpdateStrategy<Reservation> strategy = (ReservationUpdateStrategy<Reservation>)
                strategyFactory.getStrategy(newData.getClass());
        Reservation updated = strategy.update(existing, newData);
        return reservationRepository.save(updated);
    }

    /**
     * Updates the duration of an existing reservation and removes any vehicles associated with it.
     */
    @Transactional
    public Reservation updateDuration(Reservation existing, RentDuration duration) {
        checkIfStatusIsEqualNEW(existing, "Can not update duration for reservation with status: "
                + existing.getReservationStatus());

        ReservationUpdateStrategy<RentDuration> strategy = (ReservationUpdateStrategy<RentDuration>)
                strategyFactory.getStrategy(duration.getClass());
        Reservation updated = strategy.update(existing, duration);

        updated.setVehicles(null);
        return reservationRepository.save(updated);
    }

    @Transactional
    public void deleteReservation(Reservation reservation) {
        checkIfStatusIsEqualNEW(reservation, "Can not remove reservation with status: "
                + reservation.getReservationStatus());
        handleRemove(reservation);
    }

    @Transactional
    public void addVehicleToReservation(Reservation reservation, Long vehicleId) {
        checkIfStatusIsEqualNEW(reservation, "Can not add vehicle to reservation with status: " +
                reservation.getReservationStatus());
        reservationRepository.addVehicleToReservation(vehicleId, reservation.getId());
    }

    @Transactional
    public void deleteVehicleFromReservation(Reservation r, Long vehicleId) {
        checkIfStatusIsEqualNEW(r, "Can not remove vehicle from reservation with status: "
                + r.getReservationStatus());
        reservationRepository.deleteVehicleFromReservation(r.getId(), vehicleId);
    }

    /**
     * Calculates the total cost and deposit amount for a reservation.
     */
    @Transactional
    public Reservation calculateCost(Reservation reservation) {
        checkIfStatusIsEqualNEW(reservation, "When calculating the total cost, the reservation status should be NEW");
        Set<Vehicle> vehicles = (Set<Vehicle>) reservationRepository.findVehiclesByReservationId(reservation.getId());
        Reservation updated = calculator.calculateAndApplyCosts(reservation, vehicles);
        return reservationRepository.save(updated);
    }

    private Reservation buildAndSaveReservation(Client client, RentDuration duration) {
        Reservation reservation = buildReservation(client, duration);
        return reservationRepository.save(reservation);
    }

    private Reservation buildReservation(Client client, RentDuration duration) {
        RentInformation information = RentInformation.builder()
                .rentDuration(duration)
                .build();
        return Reservation.builder()
                .client(client)
                .reservationStatus(Reservation.ReservationStatus.NEW)
                .rentInformation(information)
                .build();
    }

    private void checkIfStatusIsEqualNEW(Reservation reservation, String message) {
        if (reservation.getReservationStatus() != Reservation.ReservationStatus.NEW) {
            throw new IllegalArgumentException(message);
        }
    }

    private void handleRemove(Reservation reservation) {
        reservation.setClient(null);
        reservation.setVehicles(null);
        reservationRepository.deleteById(reservation.getId());
    }
}

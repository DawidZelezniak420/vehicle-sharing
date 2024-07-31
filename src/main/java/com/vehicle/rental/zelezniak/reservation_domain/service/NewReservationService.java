package com.vehicle.rental.zelezniak.reservation_domain.service;

import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.reservation_domain.model.util.ReservationUpdateVisitor;
import com.vehicle.rental.zelezniak.reservation_domain.repository.ReservationRepository;
import com.vehicle.rental.zelezniak.user_domain.model.client.Client;
import com.vehicle.rental.zelezniak.user_domain.service.ClientService;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.rental.zelezniak.vehicle_domain.service.VehicleService;
import com.vehicle.rental.zelezniak.vehicle_domain.service.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewReservationService {

    private final ClientService clientService;
    private final VehicleValidator vehicleValidator;
    private final VehicleService vehicleService;
    private final ReservationCalculator reservationCalculator;
    private final ReservationUpdateVisitor updateVisitor;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation addNewReservation(
            Long clientId) {
        Client client = clientService.findById(clientId);
        return buildAndSaveReservation(client);
    }

    @Transactional
    public Reservation update(
            Reservation existing,
            Reservation newData) {
        checkIfStatusIsNew(existing,
                "Can not update reservation with status "
                        + existing.getReservationStatus());
        Reservation reservation = existing.updateReservation(
                updateVisitor, newData);
        reservationRepository.save(reservation);
        return reservation;
    }

    @Transactional
    public void remove(
            Reservation reservation) {
        checkIfStatusIsNew(reservation,
                "Can not remove reservation with status "
                        + reservation.getReservationStatus());
        handleRemove(reservation);
    }

    @Transactional
    public void addVehicleToReservation(
            Reservation reservation,
            Long vehicleId){
        Vehicle vehicle = vehicleService.findById(vehicleId);
        reservation.addVehicle(vehicle);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void removeVehicleFromReservation(
            Long reservationId,
            Long vehicleId){
        reservationRepository.deleteVehicleFromReservation(
                reservationId,vehicleId);
    }

    private Reservation buildAndSaveReservation(
            Client client) {
        Reservation reservation = buildReservation(client);
        reservationRepository.save(reservation);
        return reservation;
    }

    private Reservation buildReservation(
            Client client) {
        return Reservation.builder()
                .client(client)
                .reservationStatus(Reservation.ReservationStatus.NEW)
                .build();
    }

    private void checkIfStatusIsNew(
            Reservation reservation,
            String message) {
        if (reservation.getReservationStatus() !=
                Reservation.ReservationStatus.NEW) {
            throw new IllegalArgumentException(message);
        }
    }

    private void handleRemove(
            Reservation reservation) {
        reservation.setClient(null);
        reservation.setVehicles(null);
        reservationRepository.deleteById(
                reservation.getId());
    }
}

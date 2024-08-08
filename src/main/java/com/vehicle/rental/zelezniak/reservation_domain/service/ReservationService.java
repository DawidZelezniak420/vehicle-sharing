package com.vehicle.rental.zelezniak.reservation_domain.service;

import com.vehicle.rental.zelezniak.common_value_objects.RentDuration;
import com.vehicle.rental.zelezniak.rent_domain.service.RentService;
import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.reservation_domain.model.util.ReservationCreationRequest;
import com.vehicle.rental.zelezniak.reservation_domain.repository.ReservationRepository;
import com.vehicle.rental.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.vehicle.rental.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;
import static com.vehicle.rental.zelezniak.util.validation.InputValidator.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RentService rentService;
    private final InputValidator inputValidator;
    private final NewReservationService newReservationService;


    @Transactional(readOnly = true)
    public Page<Reservation> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    @Transactional
    public void add(ReservationCreationRequest request) {
        inputValidator.throwExceptionIfObjectIsNull(request, "Reservation creation request" + CAN_NOT_BE_NULL);
        newReservationService.addNewReservation(request);
    }

    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        validateReservationId(id);
        return findReservation(id);
    }

    @Transactional
    public Reservation updateReservation(Long id, Reservation newData) {
        validateReservationId(id);
        validateReservation(newData);
        Reservation r = findReservation(id);
        return newReservationService.updateReservation(r, newData);
    }

    @Transactional
    public Reservation updateDuration(Long id, RentDuration duration) {
        validateReservationId(id);
        inputValidator.throwExceptionIfObjectIsNull(duration, "Rent duration" + CAN_NOT_BE_NULL);
        Reservation r = findReservation(id);
        return newReservationService.updateDuration(r, duration);
    }

    @Transactional
    public void delete(Long id) {
        validateReservationId(id);
        Reservation r = findReservation(id);
        newReservationService.deleteReservation(r);
    }

    @Transactional
    public void addVehicleToReservation(Long id, Long vehicleId) {
        validateReservationId(id);
        validateVehicleId(vehicleId);
        Reservation r = findReservation(id);
        newReservationService.addVehicleToReservation(r, vehicleId);
    }

    @Transactional
    public void deleteVehicleFromReservation(Long id, Long vehicleId) {
        validateReservationId(id);
        validateVehicleId(vehicleId);
        Reservation r = findReservation(id);
        newReservationService.deleteVehicleFromReservation(r, vehicleId);
    }

    @Transactional
    public Reservation calculateCost(Long id) {
        validateReservationId(id);
        Reservation r = findReservation(id);
        return newReservationService.calculateCost(r);
    }

    private void validateReservation(Reservation reservation) {
        inputValidator.throwExceptionIfObjectIsNull(reservation, RESERVATION_NOT_NULL);
    }

    private void validateReservationId(Long id) {
        inputValidator.throwExceptionIfObjectIsNull(id, RESERVATION_ID_NOT_NULL);
    }

    private void validateVehicleId(Long id) {
        inputValidator.throwExceptionIfObjectIsNull(id, VEHICLE_ID_NOT_NULL);
    }

    private Reservation findReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation with id " + id + " does not exist."));
    }
}

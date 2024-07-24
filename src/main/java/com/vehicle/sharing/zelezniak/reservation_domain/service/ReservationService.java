package com.vehicle.sharing.zelezniak.reservation_domain.service;

import com.vehicle.sharing.zelezniak.rent_domain.service.RentService;
import com.vehicle.sharing.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.sharing.zelezniak.reservation_domain.model.util.ReservationCreationRequest;
import com.vehicle.sharing.zelezniak.reservation_domain.model.util.ReservationUpdateVisitor;
import com.vehicle.sharing.zelezniak.reservation_domain.repository.ReservationRepository;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RentService rentService;
    private final ReservationUpdateVisitor updateVisitor;
    private final ReservationBuilder reservationBuilder;
    private final InputValidator inputValidator;

    @Transactional(readOnly = true)
    public Collection<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void createReservation(
            ReservationCreationRequest request) {
        checkIfNotNull(request,
                "Reservation creation request"
                        + CAN_NOT_BE_NULL);
        handleAddReservation(request);
    }

    private void handleAddReservation(
            ReservationCreationRequest request) {
        Reservation reservation = reservationBuilder.buildReservation(
                request);
        reservationRepository.save(reservation);
    }

    private <T> void checkIfNotNull(
            T input, String message) {
        inputValidator.throwExceptionIfObjectIsNull(
                input, message);
    }
}

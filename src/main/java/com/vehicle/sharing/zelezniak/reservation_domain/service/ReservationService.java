package com.vehicle.sharing.zelezniak.reservation_domain.service;

import com.vehicle.sharing.zelezniak.rent_domain.service.RentService;
import com.vehicle.sharing.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.sharing.zelezniak.reservation_domain.model.util.ReservationUpdateVisitor;
import com.vehicle.sharing.zelezniak.reservation_domain.repository.ReservationRepository;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.vehicle.sharing.zelezniak.util.validation.InputValidator.CLIENT_ID_NOT_NULL;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RentService rentService;
    private final ReservationUpdateVisitor updateVisitor;
    private final NewReservationService reservationBuilder;
    private final InputValidator inputValidator;
    private final NewReservationService newReservationService;


    @Transactional(readOnly = true)
    public Collection<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void createReservation(Long clientId) {
        checkIfNotNull(clientId,CLIENT_ID_NOT_NULL);
        newReservationService.addNewReservation(clientId);
    }

    private <T> void checkIfNotNull(
            T input, String message) {
        inputValidator.throwExceptionIfObjectIsNull(
                input, message);
    }
}

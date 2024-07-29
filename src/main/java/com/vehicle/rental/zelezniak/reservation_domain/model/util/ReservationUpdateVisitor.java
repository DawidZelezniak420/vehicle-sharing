package com.vehicle.rental.zelezniak.reservation_domain.model.util;

import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationUpdateVisitor {

    public Reservation updateReservation(
            Reservation existingRent,
            Reservation newData) {
        return existingRent.toBuilder()
                .estimatedCost(newData.getEstimatedCost())
                .reservationStatus(newData.getReservationStatus())
                .reservationStatus(newData.getReservationStatus())
                .client(newData.getClient())
                .vehicles(newData.getVehicles())
                .build();
    }
}

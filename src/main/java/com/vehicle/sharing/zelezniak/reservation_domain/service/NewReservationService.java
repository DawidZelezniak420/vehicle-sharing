package com.vehicle.sharing.zelezniak.reservation_domain.service;

import com.vehicle.sharing.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
class NewReservationService {

    private final ClientService clientService;
    private final VehicleValidator vehicleValidator;
    private final VehicleService vehicleService;
    private final ReservationCalculator reservationCalculator;

    private final Map<Long, Reservation> newReservations = new HashMap<>();
    private Long idCounter = 0L;

    public Reservation addNewReservation(
            Long clientId) {
        Client client = clientService.findById(clientId);
        Reservation reservation = buildReservationForClient(client);
        newReservations.put(++idCounter, reservation);
        return reservation;
    }

    public Reservation updateReservation(
            Long id,Reservation newData){
     checkIfKeyExists(id);
     newReservations.put(id,newData);
     return newData;
    }

    public void removeReservation(Long id){
        newReservations.remove(id);
    }

    private Reservation buildReservationForClient(
            Client client) {
        return Reservation.builder()
                .id(idCounter)
                .client(client)
                .reservationStatus(Reservation.ReservationStatus.NEW)
                .build();
    }

    private void checkIfKeyExists(Long id) {
        if(!newReservations.containsKey(id)){
            throw new IllegalArgumentException(
                    "Reservation not found");
        }
    }
}

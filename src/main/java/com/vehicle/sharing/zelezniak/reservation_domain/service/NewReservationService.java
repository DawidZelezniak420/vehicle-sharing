package com.vehicle.sharing.zelezniak.reservation_domain.service;

import com.vehicle.sharing.zelezniak.common_value_objects.Money;
import com.vehicle.sharing.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.sharing.zelezniak.reservation_domain.model.util.ReservationCreationRequest;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
class ReservationBuilder {

    private final ClientService clientService;
    private final VehicleValidator vehicleValidator;
    private final VehicleService vehicleService;
    private final ReservationCalculator reservationCalculator;


}

package com.vehicle.rental.zelezniak.config;

import com.vehicle.rental.zelezniak.common_value_objects.Location;
import com.vehicle.rental.zelezniak.common_value_objects.Money;
import com.vehicle.rental.zelezniak.common_value_objects.RentDuration;
import com.vehicle.rental.zelezniak.common_value_objects.RentInformation;
import com.vehicle.rental.zelezniak.common_value_objects.address.City;
import com.vehicle.rental.zelezniak.common_value_objects.address.Street;
import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.reservation_domain.model.util.ReservationCreationRequest;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class ReservationCreator {

    @Autowired
    private ClientCreator clientCreator;

    @Autowired
    private VehicleCreator vehicleCreator;

    public Reservation createReservationWithId5() {
        return Reservation.builder()
                .id(5L)
                .reservationStatus(Reservation.ReservationStatus.COMPLETED)
                .estimatedCost(new Money(BigDecimal.valueOf(150.00)))
                .rentInformation(buildRentInformation())
                .vehicles(addVehicleWithId5())
                .client(clientCreator.createClientWithId5())
                .build();
    }

    public ReservationCreationRequest createReservationCreationRequest() {
        return ReservationCreationRequest.builder()
                .reservation(buildTestReservation())
                .clientId(5L)
                .vehiclesIds(createVehiclesIds())
                .build();
    }

    private RentInformation buildRentInformation() {
        return RentInformation.builder()
                .rentDuration(RentDuration.builder()
                        .rentalStart(LocalDateTime.of(2024, 7, 7, 10, 0, 0))
                        .rentalEnd(LocalDateTime.of(2024, 7, 10, 10, 0, 0))
                        .build())
                .pickUpLocation(buildLocation())
                .dropOffLocation(buildLocation())
                .build();
    }

    private Location buildLocation() {
        return Location.builder()
                .city(new City("Lublin"))
                .street(new Street("Turystyczna"))
                .additionalInformation("Next to the Leclerc mall")
                .build();
    }

    private Set<Vehicle> addVehicleWithId5() {
        Set<Vehicle> vehicles = new HashSet<>();
        vehicles.add(vehicleCreator.createCarWithId5());
        return vehicles;
    }

    private Reservation buildTestReservation() {
        return Reservation.builder()
                .rentInformation(buildRentInformation())
                .build();
    }

    private Set<Long> createVehiclesIds() {
        Set<Long> ids = new HashSet<>();
        ids.add(5L);
        ids.add(6L);
        return ids;
    }
}

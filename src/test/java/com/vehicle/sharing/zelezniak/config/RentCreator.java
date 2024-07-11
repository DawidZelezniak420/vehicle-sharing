package com.vehicle.sharing.zelezniak.config;

import com.vehicle.sharing.zelezniak.common_value_objects.*;
import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.model.rent_value_objects.Location;
import com.vehicle.sharing.zelezniak.rent_domain.model.rent_value_objects.RentInformation;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class RentCreator {

    @Autowired
    private ClientCreator clientCreator;

    @Autowired
    private VehicleCreator vehicleCreator;

    public Rent createRentWithId5() {
        return Rent.builder()
                .id(5L)
                .rentStatus(Rent.RentStatus.ACTIVE)
                .rentInformation(buildRentInformation())
                .vehicles(addVehicleWithId5())
                .clients(addClientWithId5())
                .build();
    }

    private RentInformation buildRentInformation() {
        return RentInformation.builder()
                .totalCost(new Money(BigDecimal.valueOf(150.00)))
                .rentalStart(LocalDateTime.of(2024, 7, 7, 10, 0, 0))
                .rentalEnd(LocalDateTime.of(2024, 7, 10, 10, 0, 0))
                .pickUpLocation(buildLocation())
                .build();
    }

    private Location buildLocation() {
        return Location.builder()
                .country(new Country("Poland"))
                .district(new District("Lubelskie"))
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

    private Set<Client> addClientWithId5() {
        Set<Client> clients = new HashSet<>();
        clients.add(clientCreator.createClientWithId5());
        return clients;
    }

}

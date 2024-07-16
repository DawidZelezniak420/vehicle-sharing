package com.vehicle.sharing.zelezniak.rent_domain.service;

import com.vehicle.sharing.zelezniak.common_value_objects.Money;
import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.model.util.RentCalculator;
import com.vehicle.sharing.zelezniak.rent_domain.model.util.RentCreationRequest;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor
class RentBuilder {

    private final ClientService clientService;
    private final VehicleValidator vehicleValidator;
    private final VehicleService vehicleService;
    private final RentCalculator rentCalculator;

    Rent buildRent(
            RentCreationRequest request) {
        Rent basic = request.getRent();
        Rent rent = basic.toBuilder()
                .vehicles(handleGetActiveVehicles(
                        request.getVehiclesIds()))
                .client(clientService.findById(
                        request.getClientId()))
                .rentStatus(Rent.RentStatus.ACTIVE)
                .build();
        calculateAndSetTotalCost(rent);
        return rent;
    }

    private Set<Vehicle> handleGetActiveVehicles(
            Set<Long> vehiclesIds) {
        Set<Vehicle> vehicles = vehicleService.findVehiclesByIDs(
                vehiclesIds);
        validateVehicles(vehicles);
        return vehicles;
    }

    private void validateVehicles(Collection<Vehicle> vehicles) {
        vehicleValidator.checkIfVehiclesAreActive(
                vehicles);
    }

    private void calculateAndSetTotalCost(Rent rent) {
        Money c = rentCalculator.calculateTotalCost(rent);
        rent.setTotalCost(c);
    }
}

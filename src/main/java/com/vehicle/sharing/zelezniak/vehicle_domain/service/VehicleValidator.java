package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class VehicleValidator {

    private final VehicleRepository vehicleRepository;

    public void throwExceptionIfVehicleExists(
            RegistrationNumber registrationNumber) {
        if (vehicleRepository.existsByVehicleInformationRegistrationNumber(
                registrationNumber)) {
            String message = createMessage(registrationNumber);
            throwException(message);
        }
    }

    public void checkIfVehicleCanBeUpdated(
            RegistrationNumber registrationNumber,
            Vehicle newData) {
        RegistrationNumber newDataRegistrationNumber = newData.getRegistrationNumber();
        if (registrationsAreNotSame(
                registrationNumber, newDataRegistrationNumber)
                && vehicleRegistrationNumberExists(newDataRegistrationNumber)) {
            String message = createMessage(newDataRegistrationNumber);
            throwException(message);
        }
    }

    public void checkIfVehiclesAreActive(
            Collection<Vehicle> vehicles) {
        checkVehiclesStatus(vehicles);
    }

    private void throwException(
            String message) {
        throw new IllegalArgumentException(message);
    }

    private String createMessage(
            RegistrationNumber n) {
        return "Vehicle with registration number : " + n.getRegistration() + " already exists";
    }

    private boolean registrationsAreNotSame(
            RegistrationNumber registrationNumber,
            RegistrationNumber newDataRegistrationNumber) {
        return !registrationNumber.equals(newDataRegistrationNumber);
    }

    private boolean vehicleRegistrationNumberExists(
            RegistrationNumber newDataRegistrationNumber) {
        return vehicleRepository.existsByVehicleInformationRegistrationNumber(
                newDataRegistrationNumber);
    }

    private void checkVehiclesStatus(Collection<Vehicle> vehiclesFromDb) {
        vehiclesFromDb.stream()
                .filter(this::statusIsUnavailable)
                .findFirst()
                .ifPresent(vehicle -> {
                    var information = vehicle.getVehicleInformation();
                    throwException("Vehicle " + information.getBrand()
                            + " " + information.getModel()
                            + ",with registration number:"
                            + vehicle.getRegistrationNumber()
                            + " is unavailable.");
                });
    }

    private boolean statusIsUnavailable(Vehicle vehicle) {
        return vehicle.getStatus() == Vehicle.Status.UNAVAILABLE;
    }
}

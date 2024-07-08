package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleValidator {

    private final VehicleRepository vehicleRepository;

    public void throwExceptionIfVehicleExists(
            String registrationNumber) {
        if (vehicleRepository.existsByVehicleInformationRegistrationNumber(
                registrationNumber)) {
            throw new IllegalArgumentException(
                    createMessage(registrationNumber));
        }
    }

    public void checkIfVehicleCanBeUpdated(
            String registrationNumber,
            Vehicle newData) {
        String newDataRegistrationNumber = newData.getRegistrationNumber();
        if (registrationsAreNotSame(
                registrationNumber, newDataRegistrationNumber)
                && vehicleByRegistrationExists(newDataRegistrationNumber)) {
            throw new IllegalArgumentException(createMessage(newDataRegistrationNumber));
        }
    }

    private String createMessage(String n) {
        return "Vehicle with registration number : " + n + " already exists";
    }

    private boolean registrationsAreNotSame(
            String registrationNumber, String newDataRegistrationNumber) {
        return !registrationNumber.equals(newDataRegistrationNumber);
    }

    private boolean vehicleByRegistrationExists(String newDataRegistrationNumber) {
        return vehicleRepository.existsByVehicleInformationRegistrationNumber(
                newDataRegistrationNumber);
    }
}

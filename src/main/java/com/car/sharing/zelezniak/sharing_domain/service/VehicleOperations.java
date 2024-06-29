package com.car.sharing.zelezniak.sharing_domain.service;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import com.car.sharing.zelezniak.util.validation.DataValidator;
import com.car.sharing.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VehicleOperations implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final DataValidator dataValidator;
    private final VehicleUpdateVisitor updateVisitor;

    public Collection<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle findById(Long id) {
        validateVehicleId(id);
        return findVehicle(id);
    }

    public void addVehicle(Vehicle vehicle) {
        validateVehicle(vehicle);
        vehicleValidator.throwExceptionIfVehicleExists(
                vehicle.getRegistrationNumber());
        saveVehicle(vehicle);
    }

    public Vehicle updateVehicle(
            Long id, Vehicle newData) {
        validateVehicleId(id);
        validateVehicle(newData);
        Vehicle vehicleFromDb = findVehicle(id);
        return validateAndUpdateVehicle(
                vehicleFromDb, newData);
    }

    private void validateVehicleId(Long id) {
        dataValidator.throwExceptionIfObjectIsNull(
                id, InputValidator.VEHICLE_ID_NOT_NULL);
    }

    private void validateVehicle(Vehicle vehicle) {
        dataValidator.throwExceptionIfObjectIsNull(
                vehicle, InputValidator.VEHICLE_NOT_NULL);
    }

    private Vehicle findVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Vehicle with id: " + id + " does not exists."));
    }

    private void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    private Vehicle validateAndUpdateVehicle(
            Vehicle vehicleFromDb, Vehicle newData) {
        vehicleValidator.checkIfVehicleCanBeUpdated(
                vehicleFromDb.getRegistrationNumber(), newData);
        vehicleFromDb.update(updateVisitor, newData);
        saveVehicle(vehicleFromDb);
        return vehicleFromDb;
    }
}

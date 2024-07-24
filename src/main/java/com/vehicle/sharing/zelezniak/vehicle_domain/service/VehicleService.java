package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.common_value_objects.RentDuration;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util.VehicleUpdateVisitor;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final InputValidator inputValidator;
    private final VehicleUpdateVisitor updateVisitor;
    private final VehicleCriteriaSearch criteriaSearch;
    private final AvailableVehiclesRetriever vehiclesRetriever;

    @Transactional(readOnly = true)
    public Collection<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        checkIfNotNull(id, InputValidator.VEHICLE_ID_NOT_NULL);
        return findVehicle(id);
    }

    @Transactional
    public void add(Vehicle vehicle) {
        checkIfNotNull(vehicle, InputValidator.VEHICLE_NOT_NULL);
        vehicleValidator.throwExceptionIfVehicleExists(
                vehicle.getRegistrationNumber());
        save(vehicle);
    }

    @Transactional
    public void update(
            Long id, Vehicle newData) {
        checkIfNotNull(id, InputValidator.VEHICLE_ID_NOT_NULL);
        checkIfNotNull(newData, InputValidator.VEHICLE_NOT_NULL);
        Vehicle vehicleFromDb = findVehicle(id);
        validateAndUpdateVehicle(
                vehicleFromDb, newData);
    }

    @Transactional
    public void delete(Long id) {
        checkIfNotNull(id, InputValidator.VEHICLE_ID_NOT_NULL);
        handleDeleteVehicle(id);
    }

    @Transactional(readOnly = true)
    public <T> Collection<Vehicle> findByCriteria(
            String criteriaType, T value) {
        checkIfNotNull(criteriaType,
                "Criteria type" + CAN_NOT_BE_NULL);
        checkIfNotNull(value,
                "Searched value" + CAN_NOT_BE_NULL);
        return criteriaSearch.findVehiclesByCriteria(
                criteriaType, value);
    }

    @Transactional(readOnly = true)
    public Collection<Vehicle> findVehiclesByIDs(
            Set<Long> vehiclesIds) {
        return vehicleRepository.findVehiclesByIdIn(
                vehiclesIds);
    }

    @Transactional(readOnly = true)
    public Collection<Vehicle> findAvailableVehicles(
            RentDuration duration) {
        checkIfNotNull(duration,
                "Duration"+ CAN_NOT_BE_NULL);
             return vehiclesRetriever.findAvailableVehiclesInPeriod(
                     duration);
    }

    private <T> void checkIfNotNull(
            T input, String message) {
        inputValidator.throwExceptionIfObjectIsNull(
                input, message);
    }

    private Vehicle findVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Vehicle with id: " + id + " does not exists."));
    }

    private void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    private void validateAndUpdateVehicle(
            Vehicle vehicleFromDb, Vehicle newData) {
        vehicleValidator.checkIfVehiclesHasSameTypes(
                vehicleFromDb,newData);
        vehicleValidator.checkIfVehicleCanBeUpdated(
                vehicleFromDb.getRegistrationNumber(), newData);
        Vehicle updatedVehicle = vehicleFromDb.update(updateVisitor, newData);
        save(updatedVehicle);
    }

    private void handleDeleteVehicle(
            Long id) {
        Vehicle vehicle = findVehicle(id);
        vehicleRepository.delete(vehicle);
    }
}

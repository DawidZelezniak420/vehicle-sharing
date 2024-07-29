package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.common_value_objects.RentDuration;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util.CriteriaSearchRequest;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util.VehicleUpdateVisitor;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
    public Page<Vehicle> findAll(
            Pageable pageable) {
        return vehicleRepository.findAll(pageable);
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
    public <T> Page<Vehicle> findByCriteria(
            CriteriaSearchRequest<T> searchRequest,
            Pageable pageable) {
        checkIfNotNull(searchRequest,
                "Criteria search request"
                        + CAN_NOT_BE_NULL);
        return criteriaSearch.findVehiclesByCriteria(
                searchRequest,pageable);
    }

    @Transactional(readOnly = true)
    public Page<Vehicle> findAvailableVehicles(
            RentDuration duration,
            Pageable pageable) {
        checkIfNotNull(duration,
                "Duration"+ CAN_NOT_BE_NULL);
             return vehiclesRetriever.findAvailableVehiclesInPeriod(
                     duration,pageable);
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

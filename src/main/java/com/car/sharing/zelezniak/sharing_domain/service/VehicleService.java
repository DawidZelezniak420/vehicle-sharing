package com.car.sharing.zelezniak.sharing_domain.service;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import com.car.sharing.zelezniak.util.validation.InputValidator;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.car.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;
import static com.car.sharing.zelezniak.util.validation.InputValidator.*;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final InputValidator inputValidator;
    private final VehicleUpdateVisitor updateVisitor;
    private final VehicleCriteriaSearch criteriaSearch;
    private final EntityManager entityManager;


    @Transactional(readOnly = true)
    public Collection<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        checkIfNotNull(id, VEHICLE_ID_NOT_NULL);
        return findVehicle(id);
    }

    @Transactional
    public void add(Vehicle vehicle) {
        checkIfNotNull(vehicle, VEHICLE_NOT_NULL);
        vehicleValidator.throwExceptionIfVehicleExists(
                vehicle.getRegistrationNumber());
        persistVehicle(vehicle);
    }

    @Transactional
    public void update(
            Long id, Vehicle newData) {
        checkIfNotNull(id, VEHICLE_ID_NOT_NULL);
        checkIfNotNull(newData, VEHICLE_NOT_NULL);
        Vehicle vehicleFromDb = findVehicle(id);
        validateAndUpdateVehicle(
                vehicleFromDb, newData);
    }

    @Transactional
    public void delete(Long id) {
        checkIfNotNull(id, VEHICLE_ID_NOT_NULL);
        handleDeleteVehicle(id);
    }

    @Transactional(readOnly = true)
    public <T> Collection<Vehicle> findByCriteria(String criteriaType, T value) {
        checkIfNotNull(criteriaType, "Criteria type" + CAN_NOT_BE_NULL);
        checkIfNotNull(value, "Searched value" + CAN_NOT_BE_NULL);
        return criteriaSearch.findVehiclesByCriteria(
                criteriaType, value);
    }

    private <T> void checkIfNotNull(T input, String message) {
        inputValidator.throwExceptionIfObjectIsNull(
                input, message);
    }

    private Vehicle findVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Vehicle with id: " + id + " does not exists."));
    }

    private void persistVehicle(Vehicle vehicle) {
        entityManager.persist(vehicle);
        entityManager.flush();
    }

    private void validateAndUpdateVehicle(
            Vehicle vehicleFromDb, Vehicle newData) {
        vehicleValidator.checkIfVehicleCanBeUpdated(
                vehicleFromDb.getRegistrationNumber(), newData);
        Vehicle updatedVehicle = vehicleFromDb.update(updateVisitor, newData);
        mergeVehicle(updatedVehicle);
    }

    private void mergeVehicle(Vehicle vehicle) {
        entityManager.merge(vehicle);
        entityManager.flush();
    }

    private void handleDeleteVehicle(Long id) {
        Vehicle vehicle = findVehicle(id);
        entityManager.remove(vehicle);
    }
}

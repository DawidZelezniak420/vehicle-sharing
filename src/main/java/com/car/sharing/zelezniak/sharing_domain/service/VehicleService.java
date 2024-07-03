package com.car.sharing.zelezniak.sharing_domain.service;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Year;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import com.car.sharing.zelezniak.util.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

import static com.car.sharing.zelezniak.util.validation.InputValidator.*;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final DataValidator dataValidator;
    private final VehicleUpdateVisitor updateVisitor;

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
        saveVehicle(vehicle);
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
        CriteriaType criteria = CriteriaType.getCriteriaFromString(criteriaType);
        Map<CriteriaType, Function<T, Collection<Vehicle>>> criteriaMap = initializeCriteriaMap();
        Function<T, Collection<Vehicle>> queryFunction = criteriaMap.get(criteria);
        return handleExecuteFunction(value, queryFunction);
    }

    private <T> void checkIfNotNull(T input, String message) {
        dataValidator.throwExceptionIfObjectIsNull(
                input, message);
    }

    private Vehicle findVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Vehicle with id: " + id + " does not exists."));
    }

    private void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    private void validateAndUpdateVehicle(
            Vehicle vehicleFromDb, Vehicle newData) {
        vehicleValidator.checkIfVehicleCanBeUpdated(
                vehicleFromDb.getRegistrationNumber(), newData);
        Vehicle updatedVehicle = vehicleFromDb.update(updateVisitor, newData);
        saveVehicle(updatedVehicle);
    }

    private void handleDeleteVehicle(Long id) {
        Vehicle toDelete = findVehicle(id);
        vehicleRepository.delete(toDelete);
    }

    private <T> Map<CriteriaType,
            Function<T, Collection<Vehicle>>> initializeCriteriaMap() {
        Map<CriteriaType, Function<T, Collection<Vehicle>>> result = new EnumMap<>(CriteriaType.class);
        result.put(CriteriaType.BRAND, value -> vehicleRepository.findByVehicleInformationBrand((String) value));
        result.put(CriteriaType.MODEL, value -> vehicleRepository.findByVehicleInformationModel((String) value));
        result.put(CriteriaType.REGISTRATION_NUMBER, value -> vehicleRepository.findByVehicleInformationRegistrationNumber((String) value));
        result.put(CriteriaType.PRODUCTION_YEAR, value -> {
            Year year = new Year(getYearValue(value));
            return vehicleRepository.findByVehicleInformationProductionYear(year);
        });
        return result;
    }

    private <T> int getYearValue(T value) {
        int yearValue = 0;
        if (value instanceof String s) {
            yearValue = Integer.parseInt((s));
        } else if (value instanceof Number) {
            yearValue = (int) value;
        }
        return yearValue;
    }

    private <T> Collection<Vehicle> handleExecuteFunction(
            T value, Function<T, Collection<Vehicle>> queryFunction) {
        checkIfNotNull(queryFunction, "Wrong criteria type");
        return queryFunction.apply(value);
    }
}

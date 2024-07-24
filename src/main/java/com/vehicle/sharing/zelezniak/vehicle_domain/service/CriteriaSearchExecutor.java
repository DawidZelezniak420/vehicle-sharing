package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.Year;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class CriteriaSearchExecutor {

    private static final String CAN_NOT_CONVERT = "Can not convert value to ";

    private final VehicleRepository vehicleRepository;

    public Collection<Vehicle> findByBrand(
            Object brand) {
        return vehicleRepository.findByVehicleInformationBrand(
                (String) brand);
    }

    public Collection<Vehicle> findByModel(
            Object model) {
        return vehicleRepository.findByVehicleInformationModel(
                (String) model);
    }

    public Collection<Vehicle> findByRegistrationNumber(
            Object registration) {
        RegistrationNumber number = getRegistrationValue(registration);
        return vehicleRepository.findByVehicleInformationRegistrationNumber(
                number);
    }

    public Collection<Vehicle> findByProductionYear(
            Object productionYear) {
        Year year = new Year(getYearValue(productionYear));
        return vehicleRepository.findByVehicleInformationProductionYear(
                year);
    }

    public Collection<Vehicle> findByStatus(
            Object statusValue) {
        String status = (String) statusValue;
        Vehicle.Status s = Vehicle.Status.getStatusFromString(
                status);
        return vehicleRepository.findByStatus(s);
    }

    private <T> RegistrationNumber getRegistrationValue(
            T value) {
        if (value instanceof RegistrationNumber number) {
            return number;
        } else if (value instanceof String number) {
            return new RegistrationNumber(number);
        }
        String message = CAN_NOT_CONVERT + "registration number:" + value;
        throw new IllegalArgumentException(message);
    }

    private <T> int getYearValue(
            T value) {
        if (value instanceof String s) {
            return Integer.parseInt((s));
        } else if (value instanceof Number) {
            return (int) value;
        }
        String message = CAN_NOT_CONVERT + "year:" + value;
        throw new IllegalArgumentException(message);
    }
}

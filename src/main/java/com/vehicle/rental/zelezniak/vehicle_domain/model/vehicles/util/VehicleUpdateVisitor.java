package com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.util;

import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Car;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Motorcycle;
import org.springframework.stereotype.Component;

@Component
public class VehicleUpdateVisitor {

    public Car update(Car existingCar, Car newData) {
        return existingCar.toBuilder()
                .vehicleInformation(newData.getVehicleInformation())
                .doorsNumber(newData.getDoorsNumber())
                .bodyType(newData.getBodyType())
                .driveType(newData.getDriveType())
                .pricePerDay(newData.getPricePerDay())
                .status(newData.getStatus())
                .deposit(newData.getDeposit())
                .build();
    }

    public Motorcycle update(Motorcycle existingMotorcycle,
                             Motorcycle newData) {
        return existingMotorcycle.toBuilder()
                .vehicleInformation(newData.getVehicleInformation())
                .motorcycleType(newData.getMotorcycleType())
                .pricePerDay(newData.getPricePerDay())
                .status(newData.getStatus())
                .deposit(newData.getDeposit())
                .build();
    }
}

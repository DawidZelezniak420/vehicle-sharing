package com.car.sharing.zelezniak.sharingdomain.model.vehicles;

import com.car.sharing.zelezniak.sharingdomain.model.valueobjects.VehicleInformation;
import org.springframework.stereotype.Component;

@Component
public class VehicleUpdateVisitor {

    public Car update(Car existingCar, Car newData) {
        return existingCar.toBuilder()
                .vehicleInformation(updateVehicleInfo(
                        existingCar.getVehicleInformation(),
                        newData))
                .numberOfDoors(newData.getNumberOfDoors())
                .pricePerDay(newData.getPricePerDay())
                .build();
    }

    public Motorcycle update(Motorcycle existingMotorcycle,
                            Motorcycle newData) {
       return existingMotorcycle.toBuilder()
                .vehicleInformation(updateVehicleInfo(
                        existingMotorcycle.getVehicleInformation(),
                        newData))
                .pricePerDay(newData.getPricePerDay())
                .build();
    }

    private VehicleInformation updateVehicleInfo(
            VehicleInformation vehicleInformation,
            Vehicle newData) {
        return vehicleInformation.toBuilder()
                .registrationNumber(newData.getRegistrationNumber())
                .description(newData.getDescription())
                .build();
    }
}

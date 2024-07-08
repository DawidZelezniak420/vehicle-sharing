package com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util;

import com.vehicle.sharing.zelezniak.vehicle_domain.model.value_objects.VehicleInformation;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Car;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Motorcycle;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleUpdateVisitor {

    public Car update(Car existingCar, Car newData) {
        return existingCar.toBuilder()
                .vehicleInformation(updateVehicleInfo(
                        existingCar.getVehicleInformation(),
                        newData))
                .doorsNumber(newData.getDoorsNumber())
                .bodyType(newData.getBodyType())
                .driveType(newData.getDriveType())
                .pricePerDay(newData.getPricePerDay())
                .build();
    }

    public Motorcycle update(Motorcycle existingMotorcycle,
                             Motorcycle newData) {
        return existingMotorcycle.toBuilder()
                .vehicleInformation(updateVehicleInfo(
                        existingMotorcycle.getVehicleInformation(),
                        newData))
                .motorcycleType(newData.getMotorcycleType())
                .pricePerDay(newData.getPricePerDay())
                .build();
    }

    private VehicleInformation updateVehicleInfo(
            VehicleInformation actual,
            Vehicle newData) {
        var newInfo = newData.getVehicleInformation();
        return actual.toBuilder()
                .brand(newInfo.getBrand())
                .model(newInfo.getModel())
                .productionYear(newInfo.getProductionYear())
                .registrationNumber(newInfo.getRegistrationNumber())
                .description(newInfo.getDescription())
                .engine(newInfo.getEngine())
                .gearType(newInfo.getGearType())
                .build();
    }
}

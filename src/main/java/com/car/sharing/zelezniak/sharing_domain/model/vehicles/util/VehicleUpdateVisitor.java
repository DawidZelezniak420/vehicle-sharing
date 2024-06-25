package com.car.sharing.zelezniak.sharing_domain.model.vehicles.util;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.VehicleInformation;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Car;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Motorcycle;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleUpdateVisitor {

    public Car update(Car existingCar, Car newData) {
        return existingCar.toBuilder()
                .vehicleInformation(updateVehicleInfo(
                        existingCar.getVehicleInformation(),
                        newData))
                .doorsNumber(newData.getDoorsNumber())
                .carType(newData.getCarType())
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
            VehicleInformation vehicleInformation,
            Vehicle newData) {
        var newVehicleInfo = newData.getVehicleInformation();
        return vehicleInformation.toBuilder()
                .brand(newVehicleInfo.getBrand())
                .model(newVehicleInfo.getModel())
                .productionYear(newVehicleInfo.getProductionYear())
                .registrationNumber(newVehicleInfo.getRegistrationNumber())
                .description(newVehicleInfo.getDescription())
                .engine(vehicleInformation.getEngine())
                .build();
    }
}

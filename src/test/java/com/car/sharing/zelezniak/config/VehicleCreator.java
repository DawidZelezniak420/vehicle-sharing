package com.car.sharing.zelezniak.config;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.*;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VehicleCreator {

    public Vehicle createCarWithId5() {
        Engine engine = buildCarEngine();
        var information = buildCarInformation(engine);
        return Car.builder()
                .id(5L)
                .vehicleInformation(information)
                .bodyType(Car.BodyType.HATCHBACK)
                .status(Vehicle.Status.AVAILABLE)
                .driveType(Car.DriveType.FRONT_WHEEL_DRIVE)
                .pricePerDay(new Money(BigDecimal.valueOf(50.0)))
                .doorsNumber(5)
                .build();
    }

    public Vehicle createMotorcycleWithId6() {
        Engine engine = buildMotorcycleEngine();
        var information = buildMotorcycleInformation(engine);
        return Motorcycle.builder()
                .id(6L)
                .vehicleInformation(information)
                .motorcycleType(Motorcycle.MotorcycleType.SPORT)
                .status(Vehicle.Status.AVAILABLE)
                .pricePerDay(new Money(BigDecimal.valueOf(100.00)))
                .build();
    }

    public Vehicle buildVehicle5WithDifferentData() {
        Engine engine = updateEngine();
        VehicleInformation information = updateInformation(engine);
        return Car.builder()
                .id(5L)
                .vehicleInformation(information)
                .bodyType(Car.BodyType.HATCHBACK)
                .status(Vehicle.Status.AVAILABLE)
                .driveType(Car.DriveType.FOUR_WHEEL_DRIVE)
                .pricePerDay(new Money(BigDecimal.valueOf(150.0)))
                .doorsNumber(5)
                .build();
    }

    public Vehicle createTestCar() {
        Engine engine = buildTestCarEngine();
        VehicleInformation information = buildTestCarInformation(engine);
        return Car.builder()
                .vehicleInformation(information)
                .bodyType(Car.BodyType.COUPE)
                .status(Vehicle.Status.AVAILABLE)
                .driveType(Car.DriveType.FOUR_WHEEL_DRIVE)
                .pricePerDay(new Money(BigDecimal.valueOf(1000.0)))
                .doorsNumber(3)
                .build();
    }

    private Engine buildCarEngine() {
        return Engine.builder()
                .engineType("1.9TDI AVG")
                .fuelType(Engine.FuelType.DIESEL)
                .cylinders(4)
                .displacement(1900)
                .horsepower(110)
                .build();
    }

    private VehicleInformation buildCarInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Seat")
                .model("Leon 1M")
                .productionYear(new Year(2001))
                .registrationNumber("ABC55555")
                .description("Seat Leon car")
                .engine(engine)
                .gearType(VehicleInformation.GearType.MANUAL)
                .build();
    }


    private Engine buildMotorcycleEngine() {
        return Engine.builder()
                .engineType("Minarelli-Yamaha 5D1E")
                .fuelType(Engine.FuelType.GASOLINE)
                .cylinders(1)
                .displacement(125)
                .horsepower(15)
                .build();
    }


    private VehicleInformation buildMotorcycleInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Yamaha")
                .model("YZF-R125")
                .productionYear(new Year(2015))
                .registrationNumber("ABC66666")
                .description("Legendary Yamaha 125")
                .engine(engine)
                .gearType(VehicleInformation.GearType.MANUAL)
                .build();
    }

    private Engine updateEngine() {
        return Engine.builder()
                .engineType("1.9TDI AVG")
                .fuelType(Engine.FuelType.DIESEL)
                .cylinders(4)
                .displacement(1900)
                .horsepower(150)
                .build();
    }

    private VehicleInformation updateInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Seat")
                .model("Leon 1M")
                .productionYear(new Year(2001))
                .registrationNumber("ABC55555")
                .description("Tuned Seat Leon")
                .engine(engine)
                .gearType(VehicleInformation.GearType.MANUAL)
                .build();
    }

    private Engine buildTestCarEngine() {
        return Engine.builder()
                .engineType("VR38DETT")
                .fuelType(Engine.FuelType.GASOLINE)
                .cylinders(6)
                .displacement(3800)
                .horsepower(565)
                .build();
    }

    private VehicleInformation buildTestCarInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Nissan")
                .model("GT-R R35")
                .productionYear(new Year(2021))
                .registrationNumber("GTR54321")
                .description("Nissan GT-R R35 high-performance sports car")
                .engine(engine)
                .gearType(VehicleInformation.GearType.AUTOMATIC)
                .build();
    }
}

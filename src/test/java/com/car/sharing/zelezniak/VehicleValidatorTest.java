package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Engine;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Money;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.VehicleInformation;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Year;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Car;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.service.VehicleValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class VehicleValidatorTest {

    private static final Vehicle vehicleWithId5 = createCarWithId5();

    @Autowired
    private VehicleValidator validator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${create.vehicle.five}")
    private String createVehicleFive;

    @Value("${create.car.five}")
    private String createCarFive;

    @Value("${create.vehicle.six}")
    private String createVehicleSix;

    @Value("${create.motorcycle.six}")
    private String createMotorcycleSix;

    @BeforeEach
    void setupDatabase() {
        executeQueries(createVehicleFive, createCarFive,
                createVehicleSix, createMotorcycleSix);
    }

    @Test
    void shouldTestVehicleCanBeUpdated() {
        assertDoesNotThrow(() ->
                validator.checkIfVehicleCanBeUpdated(
                        vehicleWithId5.getRegistrationNumber(),
                        createTestCar()));
    }

    @Test
    void shouldTestVehicleCanNotBeUpdated() {
        String existingRegistration = "ABC66666";
        Vehicle newCarData = createTestCar();
        VehicleInformation information = newCarData
                .getVehicleInformation()
                .toBuilder()
                .registrationNumber(existingRegistration)
                .build();
        newCarData.setVehicleInformation(information);

        String vehicleWithId5Registration = vehicleWithId5.getRegistrationNumber();
        assertThrows(IllegalArgumentException.class,
                () -> validator.checkIfVehicleCanBeUpdated(
                        vehicleWithId5Registration, newCarData));
    }

    @Test
    void shouldThrowExceptionIfVehicleExists() {
        String vehicleWithId5Registration = vehicleWithId5.getRegistrationNumber();
        assertThrows(IllegalArgumentException.class,
                () -> validator.throwExceptionIfVehicleExists(
                        vehicleWithId5Registration));
    }

    @AfterEach
    void cleanupDatabase() {
        executeQueries("delete from cars", "delete from motorcycles", "delete from vehicle");
    }

    private static Vehicle createCarWithId5() {
        Engine engine = buildCarWithId5Engine();
        VehicleInformation information = buildCarWithId5Information(engine);

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

    private static Engine buildCarWithId5Engine() {
        return Engine.builder()
                .engineType("1.9TDI AVG")
                .fuelType(Engine.FuelType.DIESEL)
                .cylinders(4)
                .displacement(1900)
                .horsepower(110)
                .build();
    }

    private static VehicleInformation buildCarWithId5Information(Engine engine) {
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

    private void executeQueries(String... queries) {
        Arrays.stream(queries).forEach(jdbcTemplate::execute);
    }

    private Vehicle createTestCar() {
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

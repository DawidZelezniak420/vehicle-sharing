package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Engine;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Money;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.VehicleInformation;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Year;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Car;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Motorcycle;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import com.car.sharing.zelezniak.sharing_domain.service.VehicleService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class VehicleCriteriaSearchTest {

    private static Vehicle vehicleWithId5;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

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

    @Value("${create.vehicle.seven}")
    private String createVehicleSeven;

    @Value("${create.car.seven}")
    private String createCarSeven;

    @Value("${create.vehicle.eight}")
    private String createVehicleEight;

    @Value("${create.car.eight}")
    private String createCarEight;

    @Value("${create.vehicle.nine}")
    private String createVehicleNine;

    @Value("${create.motorcycle.nine}")
    private String createMotorcycleNine;

    @BeforeEach
    void setupDatabase() {
        executeQueries(createVehicleFive, createCarFive,
                createVehicleSix, createMotorcycleSix,
                createVehicleSeven, createCarSeven, createVehicleEight,
                createCarEight, createVehicleNine, createMotorcycleNine);
        vehicleWithId5 = createCarWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        executeQueries(
                "delete from cars",
                "delete from motorcycles",
                "delete from vehicle");
    }

    @Test
    void shouldFindVehiclesByCriteriaModel() {
        var info = vehicleWithId5.getVehicleInformation();
        Collection<Vehicle> vehicles = vehicleService.findByCriteria(
                "model", info.getModel());
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.contains(vehicleWithId5));
    }

    @Test
    void shouldFindVehiclesByCriteriaBrand() {
        Vehicle vehicle7 = vehicleService.findById(7L);
        var info = vehicle7.getVehicleInformation();
        Collection<Vehicle> vehicles = vehicleService.findByCriteria(
                "brand", info.getBrand());
        assertTrue(vehicles.contains(vehicle7));
        assertEquals(1, vehicles.size());
    }

    @Test
    void shouldFindVehiclesByCriteriaRegistrationNumber() {
        Vehicle vehicle8 = vehicleService.findById(8L);
        String vehicle8RegistrationNumber = vehicle8.getRegistrationNumber();
        Collection<Vehicle> vehicles = vehicleService.findByCriteria(
                "registration number", vehicle8RegistrationNumber);
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.contains(vehicle8));
    }

    @Test
    void shouldFindVehiclesByCriteriaProductionYear() {
        Vehicle vehicle8 = vehicleService.findById(8L);
        Vehicle vehicle9 = vehicleService.findById(9L);
        var info = vehicle8.getVehicleInformation();
        Collection<Vehicle> vehicles = vehicleService.findByCriteria(
                "production year", info.getProductionYear().getYear());
        assertEquals(2, vehicles.size());
        assertTrue(vehicles.contains(vehicle8));
        assertTrue(vehicles.contains(vehicle9));
    }

    @Test
    void shouldNotFindVehiclesByNonExistentCriteria() {
        assertThrows(IllegalArgumentException.class, () ->
                vehicleService.findByCriteria(
                        "wheels number", 4));
    }

    @Test
    void shouldFindVehiclesByCriteriaStatusAvailable() {
        Vehicle unavailableVehicle = vehicleService.findById(6L);
        unavailableVehicle.setStatus(Vehicle.Status.UNAVAILABLE);
        vehicleRepository.save(unavailableVehicle);

        assertEquals(5, vehicleRepository.count());

        List<Vehicle> vehicles = (List<Vehicle>) vehicleService.findByCriteria(
                "status","available");
        assertFalse(vehicles.contains(unavailableVehicle));
        assertEquals(4, vehicles.size());
    }

    @Test
    void shouldFindVehiclesByCriteriaStatusUnavailable() {
        Vehicle unavailableVehicle = vehicleService.findById(6L);
        unavailableVehicle.setStatus(Vehicle.Status.UNAVAILABLE);
        vehicleRepository.save(unavailableVehicle);

        assertEquals(5, vehicleRepository.count());

        List<Vehicle> vehicles = (List<Vehicle>) vehicleService.findByCriteria(
                "status","unavailable");
        assertTrue(vehicles.contains(unavailableVehicle));
        assertEquals(1, vehicles.size());
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries)
                .forEach(jdbcTemplate::execute);
    }

    private static Vehicle createCarWithId5() {
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

    private static Engine buildCarEngine() {
        return Engine.builder()
                .engineType("1.9TDI AVG")
                .fuelType(Engine.FuelType.DIESEL)
                .cylinders(4)
                .displacement(1900)
                .horsepower(110)
                .build();
    }

    private static VehicleInformation buildCarInformation(Engine engine) {
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
}

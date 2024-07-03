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
import org.junit.jupiter.api.DisplayName;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class VehicleServiceTest {

    private static Vehicle vehicleWithId5;
    private static Vehicle vehicleWithId6;

    @Autowired
    private VehicleService vehicleOperations;

    @Autowired
    @Qualifier("car")
    private Vehicle car;

    @Autowired
    @Qualifier("motorcycle")
    private Vehicle motorcycle;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

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
        vehicleWithId6 = createMotorcycleWithId6();
    }

    @AfterEach
    void cleanupDatabase() {
        executeQueries(
                "delete from cars",
                "delete from motorcycles",
                "delete from vehicle");
    }

    @Test
    void shouldReturnAllVehicles() {
        Collection<Vehicle> vehicles = vehicleOperations.findAll();
        assertTrue(vehicles.contains(vehicleWithId5));
        assertTrue(vehicles.contains(vehicleWithId6));
        assertEquals(5, vehicles.size());
    }

    @Test
    void shouldFindVehicleById() {
        Vehicle vehicle5 = vehicleOperations.findById(vehicleWithId5.getId());
        Vehicle vehicle6 = vehicleOperations.findById(vehicleWithId6.getId());
        assertEquals(vehicleWithId5, vehicle5);
        assertEquals(vehicleWithId6, vehicle6);
    }

    @Test
    void shouldNotFindVehicleById() {
        Long nonExistentId = 20L;
        assertThrows(NoSuchElementException.class, () ->
                vehicleOperations.findById(nonExistentId));
    }

    @Test
    void shouldAddVehicle() {
        Vehicle testCar = createTestCar();

        vehicleOperations.add(createTestCar());

        assertEquals(6, vehicleRepository.count());
        assertTrue(vehicleRepository.existsByVehicleInformationRegistrationNumber(
                testCar.getRegistrationNumber()));
    }

    @Test
    void shouldNotAddVehicle() {
        assertThrows(IllegalArgumentException.class, () ->
                vehicleOperations.add(vehicleWithId5));
        assertThrows(IllegalArgumentException.class, () ->
                vehicleOperations.add(vehicleWithId6));
    }

    @Test
    void shouldUpdateVehicle() {
        Long vehicle5Id = vehicleWithId5.getId();
        Vehicle newData = buildVehicle5WithDifferentData();

        vehicleOperations.update(vehicle5Id, newData);

        Vehicle updated = vehicleOperations.findById(vehicle5Id);

        assertEquals(newData, updated);
    }

    @Test
    @DisplayName("Should not update vehicle when new data contains an existing registration number")
    void shouldNotUpdateVehicle() {
        String existentRegistration = vehicleWithId6.getRegistrationNumber();
        Vehicle newData = buildVehicle5WithDifferentData();
        VehicleInformation vehicleInformation = newData.getVehicleInformation();
        VehicleInformation infoWithExistentRegistration = vehicleInformation.toBuilder()
                .registrationNumber(existentRegistration)
                .build();

        newData.setVehicleInformation(infoWithExistentRegistration);
        Long vehicleToUpdateId = vehicleWithId5.getId();

        assertThrows(IllegalArgumentException.class, () ->
                vehicleOperations.update(vehicleToUpdateId, newData));

    }

    @Test
    void shouldDeleteVehicle() {
        Long vehicle5Id = vehicleWithId5.getId();

        assertEquals(5, vehicleRepository.count());
        vehicleOperations.delete(vehicle5Id);
        assertEquals(4, vehicleRepository.count());

        List<Vehicle> all = vehicleRepository.findAll();
        assertFalse(all.contains(vehicleWithId5));
    }

    @Test
    void shouldNotDeleteVehicle() {
        Long nonExistentId = 20L;
        assertEquals(5, vehicleRepository.count());
        assertThrows(NoSuchElementException.class, () ->
                vehicleOperations.delete(nonExistentId));
        assertEquals(5, vehicleRepository.count());
    }

    @Test
    void shouldFindVehiclesByCriteriaModel() {
        var info = vehicleWithId5.getVehicleInformation();
        Collection<Vehicle> vehicles = vehicleOperations.findByCriteria(
                "model", info.getModel());
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.contains(vehicleWithId5));
    }

    @Test
    void shouldFindVehiclesByCriteriaBrand() {
        Vehicle vehicle7 = vehicleOperations.findById(7L);
        var info = vehicle7.getVehicleInformation();
        Collection<Vehicle> vehicles = vehicleOperations.findByCriteria(
                "brand", info.getBrand());
        assertTrue(vehicles.contains(vehicle7));
        assertEquals(1,vehicles.size());
    }

    @Test
    void shouldFindVehiclesByCriteriaRegistrationNumber() {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        String vehicle8RegistrationNumber = vehicle8.getRegistrationNumber();
        Collection<Vehicle> vehicles = vehicleOperations.findByCriteria(
                "registration number", vehicle8RegistrationNumber);
        assertEquals(1,vehicles.size());
        assertTrue(vehicles.contains(vehicle8));
    }

    @Test
    void shouldFindVehiclesByCriteriaProductionYear() {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        Vehicle vehicle9 = vehicleOperations.findById(9L);
        var info = vehicle8.getVehicleInformation();
        Collection<Vehicle> vehicles = vehicleOperations.findByCriteria(
                "production year", info.getProductionYear().getYear());
        assertEquals(2,vehicles.size());
        assertTrue(vehicles.contains(vehicle8));
        assertTrue(vehicles.contains(vehicle9));
    }

    @Test
    void shouldNotFindVehiclesByNonExistentCriteria() {
    assertThrows(IllegalArgumentException.class,()->
            vehicleOperations.findByCriteria(
                    "wheels number",4));
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

    private static Vehicle createMotorcycleWithId6() {
        Engine engine = buildMotorcycleEngine();
        var information = buildMotorcycleInformation(engine);
        return Motorcycle.builder()
                .id(6L)
                .vehicleInformation(information)
                .motorcycleType(Motorcycle.MotorcycleType.SPORT)
                .status(Vehicle.Status.AVAILABLE)
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

    private static Engine buildMotorcycleEngine() {
        return Engine.builder()
                .engineType("Minarelli-Yamaha 5D1E")
                .fuelType(Engine.FuelType.GASOLINE)
                .cylinders(1)
                .displacement(125)
                .horsepower(15)
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

    private static VehicleInformation buildMotorcycleInformation(Engine engine) {
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

    private Vehicle buildVehicle5WithDifferentData() {
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
}

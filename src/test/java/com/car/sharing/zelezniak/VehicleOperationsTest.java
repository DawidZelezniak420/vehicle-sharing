package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Engine;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Money;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.VehicleInformation;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Year;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Car;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.service.VehicleOperations;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class VehicleOperationsTest {

    private static Vehicle vehicleFive = createVehicleWithId5();

    @Autowired
    private VehicleOperations vehicleOperations;

    @Autowired
    @Qualifier("car")
    private Vehicle car;

    @Autowired
    @Qualifier("motorcycle")
    private Vehicle motorcycle;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${create.vehicle.five}")
    private String createVehicleFive;

    @Value("${create.car.five}")
    private String createCarFive;

    @BeforeEach
    void setupDatabase(){
        executeQueries(createVehicleFive, createCarFive);
    }

    @Test
    void shouldReturnAllVehicles(){
        Collection<Vehicle> vehicles = vehicleOperations.findAll();
        vehicles.forEach(System.out::println);
        System.out.println(vehicleFive);
        assertTrue(vehicles.contains(vehicleFive));
        assertEquals(1,vehicles.size());
    }

    @AfterEach
    void cleanupDatabase(){
    executeQueries("delete from cars",
            "delete from motorcycles",
            "delete from vehicle");
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries).forEach(jdbcTemplate::execute);
    }

    private static Vehicle createVehicleWithId5() {
        Engine engine = buildCarEngine();
        var information = buildCarVehicleInformation(engine);
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
                .engineType("1.9TDI")
                .fuelType(Engine.FuelType.DIESEL)
                .cylinders(4)
                .displacement(1900)
                .horsepower(110)
                .build();
    }

    private static VehicleInformation buildCarVehicleInformation(Engine engine) {
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

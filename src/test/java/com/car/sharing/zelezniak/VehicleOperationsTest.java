package com.car.sharing.zelezniak;

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

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class VehicleOperationsTest {

    private static Vehicle vehicleWithid5 = createVehicleWithId5();

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

    @Value("${create.car.five}")
    private String createCarFive;

    @BeforeEach
    void setupDatabase(){
        executeQueries(createCarFive);
        jdbcTemplate.execute(createCarFive);
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries).forEach(jdbcTemplate::execute);
    }

    @Test
    void shouldReturnAllVehicles(){
        Collection<Vehicle> vehicles = vehicleOperations.findAll();
        assertTrue(vehicles.contains(vehicleWithid5));
        assertEquals(1,vehicles.size());
    }

    @AfterEach
    void dropDatabase(){

    }

    private static Vehicle createVehicleWithId5() {
        return Car.builder()
                .id(5L)
                .vehicleInformation(VehicleInformation.builder()
                        .brand("Seat")
                        .model("Leon1M")
                        .productionYear(new Year(2001))
                        .registrationNumber("ABC55555")
                        .description("Seat Leon with 1.9TDI engine")
                        .build())
                .carType(Car.CarType.HATCHBACK)
                .status(Vehicle.Status.AVAILABLE)
                .build();
    }
}

package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.VehicleInformation;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class VehicleValidatorTest {

    private static Vehicle vehicleWithId5;
    private static Vehicle vehicleWithId6;

    @Autowired
    private VehicleValidator validator;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VehicleCreator vehicleCreator;
    @Autowired
    private DatabaseSetup databaseSetup;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupVehicles();
        vehicleWithId5  = vehicleCreator.createCarWithId5();
        vehicleWithId6  = vehicleCreator.createMotorcycleWithId6();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupVehicles();
    }

    @Test
    void shouldTestVehicleCanBeUpdated() {
        assertDoesNotThrow(() ->
                validator.checkIfVehicleCanBeUpdated(
                        vehicleWithId5.getRegistrationNumber(),
                        vehicleCreator.createTestCar()));
    }

    @Test
    void shouldTestVehicleCanNotBeUpdated() {
        String existingRegistration = vehicleWithId6.getRegistrationNumber();
        Vehicle newCarData = vehicleCreator.createTestCar();
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
}

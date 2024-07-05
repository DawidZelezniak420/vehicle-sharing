package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.config.*;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.VehicleInformation;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import com.car.sharing.zelezniak.sharing_domain.service.VehicleService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private VehicleCreator vehicleCreator;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupVehicles();
        vehicleWithId5 = vehicleCreator.createCarWithId5();
        vehicleWithId6 = vehicleCreator.createMotorcycleWithId6();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupVehicles();
    }

    @Test
    void shouldReturnAllVehicles() {
        Collection<Vehicle> vehicles = vehicleService.findAll();
        assertTrue(vehicles.contains(vehicleWithId5));

        Vehicle byId = vehicleService.findById(
                vehicleWithId6.getId());
        System.out.println(byId);
        System.out.println(vehicleWithId6);
        assertTrue(vehicles.contains(vehicleWithId6));
        assertEquals(5, vehicles.size());
    }

    @Test
    void shouldFindVehicleById() {
        Vehicle vehicle5 = vehicleService.findById(
                vehicleWithId5.getId());
        Vehicle vehicle6 = vehicleService.findById(
                vehicleWithId6.getId());
        assertEquals(vehicleWithId5, vehicle5);
        assertEquals(vehicleWithId6, vehicle6);
    }

    @Test
    void shouldNotFindVehicleById() {
        Long nonExistentId = 20L;
        assertThrows(NoSuchElementException.class, () ->
                vehicleService.findById(nonExistentId));
    }

    @Test
    void shouldAddVehicle() {
        Vehicle testCar = vehicleCreator.createTestCar();

        vehicleService.add(testCar);

        assertEquals(6, vehicleRepository.count());
        assertTrue(vehicleRepository.existsByVehicleInformationRegistrationNumber(
                testCar.getRegistrationNumber()));
    }

    @Test
    void shouldNotAddVehicle() {
        assertThrows(IllegalArgumentException.class, () ->
                vehicleService.add(vehicleWithId5));
        assertThrows(IllegalArgumentException.class, () ->
                vehicleService.add(vehicleWithId6));
    }

    @Test
    void shouldUpdateVehicle() {
        Long vehicle5Id = vehicleWithId5.getId();
        Vehicle newData = vehicleCreator.buildVehicle5WithDifferentData();

        vehicleService.update(vehicle5Id, newData);

        Vehicle updated = vehicleService.findById(vehicle5Id);

        assertEquals(newData, updated);
    }

    @Test
    @DisplayName("Should not update vehicle when new data contains an existing registration number")
    void shouldNotUpdateVehicle() {
        String existentRegistration = vehicleWithId6.getRegistrationNumber();
        Vehicle newData = vehicleCreator.buildVehicle5WithDifferentData();
        VehicleInformation vehicleInformation = newData.getVehicleInformation();
        VehicleInformation infoWithExistentRegistration = vehicleInformation.toBuilder()
                .registrationNumber(existentRegistration)
                .build();

        newData.setVehicleInformation(infoWithExistentRegistration);
        Long vehicleToUpdateId = vehicleWithId5.getId();

        assertThrows(IllegalArgumentException.class, () ->
                vehicleService.update(vehicleToUpdateId, newData));

    }

    @Test
    void shouldDeleteVehicle() {
        Long vehicle5Id = vehicleWithId5.getId();

        assertEquals(5, vehicleRepository.count());
        vehicleService.delete(vehicle5Id);
        assertEquals(4, vehicleRepository.count());

        List<Vehicle> all = vehicleRepository.findAll();
        assertFalse(all.contains(vehicleWithId5));
    }

    @Test
    void shouldNotDeleteVehicle() {
        Long nonExistentId = 20L;
        assertEquals(5, vehicleRepository.count());
        assertThrows(NoSuchElementException.class, () ->
                vehicleService.delete(nonExistentId));
        assertEquals(5, vehicleRepository.count());
    }
}

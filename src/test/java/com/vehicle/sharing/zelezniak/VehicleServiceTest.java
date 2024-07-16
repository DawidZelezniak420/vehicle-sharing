package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.VehicleInformation;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

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
        RegistrationNumber existentRegistration = vehicleWithId6.getRegistrationNumber();
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

    @Test
    void shouldFindVehiclesByIDsSet(){
        Set<Long> ids = new HashSet<>(Arrays.asList(5L,6L));
        Collection<Vehicle> vehicles = vehicleService.findVehiclesByIDs(ids);

        assertTrue(vehicles.contains(vehicleWithId5));
        assertTrue(vehicles.contains(vehicleWithId6));
    }
}

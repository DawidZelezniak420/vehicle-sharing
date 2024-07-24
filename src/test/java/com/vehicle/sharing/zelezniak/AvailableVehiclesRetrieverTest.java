package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.common_value_objects.RentDuration;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.RentDurationCreator;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.AvailableVehiclesRetriever;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class AvailableVehiclesRetrieverTest {

    private static Map<Long, Vehicle> vehicleMap;

    @Autowired
    private DatabaseSetup databaseSetup;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private AvailableVehiclesRetriever vehiclesRetriever;

    @Autowired
    private RentDurationCreator durationCreator;

    @Autowired
    private VehicleCreator vehicleCreator;

    @BeforeEach
    void setupData() {
        vehicleMap = new HashMap<>();
        databaseSetup.setupAllTables();
        vehicleMap.put(5L, vehicleService.findById(5L));
        vehicleMap.put(6L, vehicleService.findById(6L));
        vehicleMap.put(7L, vehicleService.findById(7L));
        vehicleMap.put(8L, vehicleService.findById(8L));
        vehicleMap.put(9L, vehicleService.findById(9L));
    }

    @AfterEach
    void dropData() {
        databaseSetup.cleanupAllTables();
        vehicleMap = null;
    }

    @Test
    void shouldFindAvailableVehiclesAvailableInPeriod1() {
        RentDuration duration = durationCreator.createDuration1();

        Collection<Vehicle> availableVehicles = vehiclesRetriever.findAvailableVehiclesInPeriod(duration);
        assertEquals(0, availableVehicles.size());
    }

    @Test
    void shouldFindAvailableVehiclesAvailableInPeriod2() {
        Vehicle motorcycle = vehicleCreator.createMotorcycleWithId6();
        motorcycle.setStatus(Vehicle.Status.UNAVAILABLE);
        vehicleService.update(6L,motorcycle);
        RentDuration duration = durationCreator.createDuration2();

        Collection<Vehicle> availableVehicles = vehiclesRetriever.findAvailableVehiclesInPeriod(duration);

        assertEquals(3, availableVehicles.size());
        assertFalse(availableVehicles.contains(vehicleMap.get(5L)));
        assertFalse(availableVehicles.contains(vehicleMap.get(6L)));
        assertTrue(availableVehicles.contains(vehicleMap.get(7L)));
        assertTrue(availableVehicles.contains(vehicleMap.get(8L)));
        assertTrue(availableVehicles.contains(vehicleMap.get(9L)));
    }

    @Test
    void shouldFindAvailableVehiclesAvailableInPeriod3() {
        RentDuration duration = durationCreator.createDuration3();

        Collection<Vehicle> availableVehicles = vehiclesRetriever.findAvailableVehiclesInPeriod(duration);

        assertEquals(1, availableVehicles.size());
        assertTrue(availableVehicles.contains(vehicleMap.get(5L)));
        assertFalse(availableVehicles.contains(vehicleMap.get(6L)));
        assertFalse(availableVehicles.contains(vehicleMap.get(7L)));
        assertFalse(availableVehicles.contains(vehicleMap.get(8L)));
        assertFalse(availableVehicles.contains(vehicleMap.get(9L)));
    }
}

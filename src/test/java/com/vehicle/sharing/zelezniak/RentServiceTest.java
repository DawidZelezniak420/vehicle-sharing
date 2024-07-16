package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.common_value_objects.Money;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.RentCreator;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.service.RentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class RentServiceTest {

    private static Rent rentWithId5;

    @Autowired
    private DatabaseSetup databaseSetup;

    @Autowired
    private RentCreator rentCreator;

    @Autowired
    private RentService rentService;

    @Autowired
    private VehicleCreator vehicleCreator;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupClients();
        databaseSetup.setupVehicles();
        databaseSetup.setupRents();
        rentWithId5 = rentCreator.createRentWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupRents();
        databaseSetup.cleanupClients();
        databaseSetup.cleanupVehicles();
    }

    @Test
    void shouldReturnAllRents() {
        List<Rent> all = (List<Rent>) rentService.findAll();
        Rent rentFromDb = all.get(0);
        assertEquals(rentWithId5, rentFromDb);
        assertEquals(1, all.size());
    }

    @Test
    void shouldFindRentById() {
        Rent byId = rentService.findById(
                rentWithId5.getId());
        assertEquals(rentWithId5, byId);
    }

    @Test
    void shouldAddRent() {
        rentWithId5.setVehicles(vehicleCreator.createSetWithVehicle5And6());
        rentWithId5.setId(1L);
        rentWithId5.setTotalCost(new Money(BigDecimal.valueOf(450.00)));
        rentService.add(rentCreator.createRentCreationRequest());
        Rent byId = rentService.findById(1L);

        assertNotNull(byId);
        assertEquals(rentWithId5, byId);
    }
}

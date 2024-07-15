package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.RentCreator;
import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.service.RentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setupDatabase(){
        databaseSetup.setupClients();
        databaseSetup.setupVehicles();
        databaseSetup.setupRents();
        rentWithId5 = rentCreator.createRentWithId5();
    }

    @AfterEach
    void cleanupDatabase(){
        databaseSetup.cleanupRents();
        databaseSetup.cleanupClients();
        databaseSetup.cleanupVehicles();
    }

    @Test
    void shouldReturnAllRents(){
        List<Rent> all = (List<Rent>) rentService.findAll();
        Rent rentFromDb = all.get(0);
        assertEquals(rentWithId5,rentFromDb);
        assertEquals(1,all.size());
    }

    @Test
    void shouldFindRentById(){
        Rent byId = rentService.findById(rentWithId5.getId());
        assertEquals(rentWithId5,byId);
    }

    @Test
    void shouldAddRent(){

    }
}

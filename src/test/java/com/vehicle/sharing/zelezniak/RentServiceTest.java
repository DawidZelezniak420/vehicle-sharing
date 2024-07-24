package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.common_value_objects.RentDuration;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.RentCreator;
import com.vehicle.sharing.zelezniak.config.RentDurationCreator;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.repository.RentRepository;
import com.vehicle.sharing.zelezniak.rent_domain.service.RentService;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private RentDurationCreator durationCreator;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupAllTables();
        rentWithId5 = rentCreator.createRentWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupAllTables();
    }

    @Test
    void shouldReturnAllRents() {
        List<Rent> all = (List<Rent>) rentService.findAll();
        Rent rentFromDb = all.get(0);

        all.forEach(Assertions::assertNotNull);
        assertEquals(rentWithId5, rentFromDb);
        assertEquals(3, all.size());
    }

    @Test
    void shouldFindRentById() {
        Rent byId = rentService.findById(
                rentWithId5.getId());
        assertEquals(rentWithId5, byId);
    }

//    @Test
//    void shouldAddRent() {
//        rentWithId5.setVehicles(vehicleCreator.createSetWithVehicle5And6());
//        rentWithId5.setId(1L);
//        rentWithId5.setTotalCost(new Money(BigDecimal.valueOf(450.00)));
//        rentService.add(rentCreator.createRentCreationRequest());
//        Rent byId = rentService.findById(1L);
//
//        assertNotNull(byId);
//        assertEquals(rentWithId5, byId);
//    }

    @Test
    void shouldFindAllClientRentsByClientId() {
        Long client5Id = 5L;

        List<Rent> allByClient5Id = (List<Rent>)
                rentService.findAllByClientId(client5Id);

        assertEquals(rentWithId5, allByClient5Id.get(0));
    }

    @Test
    void shouldFindVehiclesByRentId(){
        Long rent5Id = 5L;
        Long rent6Id = 6L;

        List<Vehicle> vehiclesByRent5Id = (List<Vehicle>)
                rentService.findVehiclesByRentId(rent5Id);
        List<Vehicle> vehiclesByRent6Id = (List<Vehicle>)
                rentService.findVehiclesByRentId(rent6Id);

        assertEquals(vehicleCreator.createCarWithId5(),
                vehiclesByRent5Id.get(0));
        assertEquals(vehicleCreator.createMotorcycleWithId6(),
                vehiclesByRent6Id.get(0));
        assertEquals(2,vehiclesByRent6Id.size());
    }

    @Test
    void shouldFindUnavailableVehicleIdsForRentInPeriod(){
        RentDuration duration = durationCreator.createDuration1();

        Collection<Long> unavailableIds = rentRepository.unavailableVehicleIdsForRentInPeriod(
                duration.getRentalStart(), duration.getRentalEnd());

        assertEquals(4,unavailableIds.size());
        assertTrue(unavailableIds.contains(6L));
        assertTrue(unavailableIds.contains(7L));
        assertTrue(unavailableIds.contains(8L));
        assertTrue(unavailableIds.contains(9L));
    }
}

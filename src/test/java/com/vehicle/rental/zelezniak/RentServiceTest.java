package com.vehicle.rental.zelezniak;

import com.vehicle.rental.zelezniak.config.RentCreator;
import com.vehicle.rental.zelezniak.config.VehicleCreator;
import com.vehicle.rental.zelezniak.rent_domain.model.Rent;
import com.vehicle.rental.zelezniak.common_value_objects.RentDuration;
import com.vehicle.rental.zelezniak.config.DatabaseSetup;
import com.vehicle.rental.zelezniak.config.RentDurationCreator;
import com.vehicle.rental.zelezniak.rent_domain.repository.RentRepository;
import com.vehicle.rental.zelezniak.rent_domain.service.RentService;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = VehicleRentalApplication.class)
@TestPropertySource("/application-test.properties")
class RentServiceTest {

    private static Rent rentWithId5;
    private static Pageable pageable = PageRequest.of(0, 5);

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
        Page<Rent> page =  rentService.findAll(pageable);
        List<Rent> rents = page.get().toList();
        Rent rentFromDb = rents.get(0);

        rents.forEach(Assertions::assertNotNull);
        assertEquals(rentWithId5, rentFromDb);
        assertEquals(3, rents.size());
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

        Page<Rent> page = rentService.findAllByClientId(
                client5Id,pageable);
        List<Rent> allByClient5Id = page.get().toList();

        assertEquals(rentWithId5, allByClient5Id.get(0));
    }

    @Test
    void shouldFindVehiclesByRentId(){
        Long rent5Id = 5L;
        Long rent6Id = 6L;

        Page<Vehicle> p1 = rentService.findVehiclesByRentId(
                rent5Id,pageable);
        List<Vehicle> vehiclesByRent5Id = p1.get().toList();

        Page<Vehicle> p2 = rentService.findVehiclesByRentId(
                rent6Id,pageable);
        List<Vehicle> vehiclesByRent6Id = p2.get().toList();

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

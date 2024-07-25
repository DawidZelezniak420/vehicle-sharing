package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Role;
import com.vehicle.sharing.zelezniak.vehicle_domain.exception.CriteriaAccessException;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util.CriteriaSearchRequest;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleCriteriaSearch;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class VehicleCriteriaSearchTest {

    private static Vehicle vehicleWithId5;

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleCriteriaSearch criteriaSearch;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private VehicleCreator vehicleCreator;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5);
        databaseSetup.setupVehicles();
        vehicleWithId5 = vehicleCreator.createCarWithId5();
    }

    @AfterEach
    void tearDown() {
        databaseSetup.cleanupVehicles();
    }

    @Test
    void shouldFindVehiclesByCriteriaModel() {
        var info = vehicleWithId5.getVehicleInformation();

        Page<Vehicle> page = criteriaSearch.findVehiclesByCriteria(
                new CriteriaSearchRequest<>("model", info.getModel()),
                pageable);

        List<Vehicle> vehicles = page.getContent();
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.contains(vehicleWithId5));
    }

    @Test
    void shouldFindVehiclesByCriteriaBrand() {
        Vehicle vehicle7 = vehicleService.findById(7L);
        var info = vehicle7.getVehicleInformation();

        Page<Vehicle> page = criteriaSearch.findVehiclesByCriteria(
                new CriteriaSearchRequest<>("brand", info.getBrand()),
                pageable);

        List<Vehicle> vehicles = page.getContent();
        assertTrue(vehicles.contains(vehicle7));
        assertEquals(1, vehicles.size());
    }

    @Test
    @DisplayName("Admin can search vehicles by registration")
    void shouldFindVehiclesByCriteriaRegistrationNumber() {
        setSecurityContextHolder("ROLE_ADMIN");
        Vehicle vehicle8 = vehicleService.findById(8L);
        RegistrationNumber vehicle8RegistrationNumber = vehicle8.getRegistrationNumber();

        Page<Vehicle> page = criteriaSearch.findVehiclesByCriteria(
                new CriteriaSearchRequest<>("registration number", vehicle8RegistrationNumber),
                pageable);

        List<Vehicle> vehicles = page.getContent();
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.contains(vehicle8));
    }

    @Test
    @DisplayName("Client can't search vehicles by registration")
    void shouldNotFindVehiclesByCriteriaRegistrationNumber() {
        setSecurityContextHolder("ROLE_USER");

        Vehicle vehicle8 = vehicleService.findById(8L);
        RegistrationNumber vehicle8RegistrationNumber = vehicle8.getRegistrationNumber();

        assertThrows(CriteriaAccessException.class, () ->
                criteriaSearch.findVehiclesByCriteria(
                        new CriteriaSearchRequest<>("registration number", vehicle8RegistrationNumber),
                        pageable));
    }

    @Test
    void shouldFindVehiclesByCriteriaProductionYear() {
        Vehicle vehicle8 = vehicleService.findById(8L);
        Vehicle vehicle9 = vehicleService.findById(9L);
        var info = vehicle8.getVehicleInformation();

        Page<Vehicle> page = criteriaSearch.findVehiclesByCriteria(
                new CriteriaSearchRequest<>("production year", info.getProductionYear().getYear()),
                pageable);

        List<Vehicle> vehicles = page.getContent();
        assertEquals(2, vehicles.size());
        assertTrue(vehicles.contains(vehicle8));
        assertTrue(vehicles.contains(vehicle9));
    }

    @Test
    void shouldNotFindVehiclesByNonExistentCriteria() {
        assertThrows(IllegalArgumentException.class, () ->
                criteriaSearch.findVehiclesByCriteria(
                        new CriteriaSearchRequest<>("wheels number", 4),
                        pageable));
    }

    @Test
    void shouldFindVehiclesByCriteriaStatusAvailable() {
        Vehicle unavailableVehicle = vehicleService.findById(6L);
        unavailableVehicle.setStatus(Vehicle.Status.UNAVAILABLE);
        vehicleRepository.save(unavailableVehicle);

        assertEquals(5, vehicleRepository.count());

        Page<Vehicle> page = criteriaSearch.findVehiclesByCriteria(
                new CriteriaSearchRequest<>("status", "available"),
                pageable);

        List<Vehicle> vehicles = page.getContent();
        assertFalse(vehicles.contains(unavailableVehicle));
        assertEquals(4, vehicles.size());
    }

    @Test
    void shouldFindVehiclesByCriteriaStatusUnavailable() {
        Vehicle unavailableVehicle = vehicleService.findById(6L);
        unavailableVehicle.setStatus(Vehicle.Status.UNAVAILABLE);
        vehicleRepository.save(unavailableVehicle);

        assertEquals(5, vehicleRepository.count());

        Page<Vehicle> page = criteriaSearch.findVehiclesByCriteria(
                new CriteriaSearchRequest<>("status", "unavailable"),
                pageable);

        List<Vehicle> vehicles = page.getContent();
        assertTrue(vehicles.contains(unavailableVehicle));
        assertEquals(1, vehicles.size());
    }

    private void setSecurityContextHolder(String role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Set<Role> authorities = new HashSet<>();
        authorities.add(new Role(role));
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password", authorities);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}

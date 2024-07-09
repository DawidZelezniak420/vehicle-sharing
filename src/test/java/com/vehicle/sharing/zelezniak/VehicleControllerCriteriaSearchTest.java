package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.config.TokenGenerator;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.Engine;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.Year;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.RoundingMode;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class VehicleControllerCriteriaSearchTest {

    private static Vehicle vehicleWithId5;
    private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VehicleService vehicleOperations;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private VehicleCreator vehicleCreator;
    @Autowired
    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupVehicles();
        vehicleWithId5 = vehicleCreator.createCarWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupVehicles();
    }

    @Test
    void shouldFindVehiclesByCriteriaModel() throws Exception {
        String criteria = "model";
        int resultSize = 1;
        String value = vehicleWithId5.getVehicleInformation().getModel();
        performCriteriaRequest(criteria, resultSize, vehicleWithId5, value, USER);
    }

    @Test
    void shouldFindVehiclesByCriteriaBrand() throws Exception {
        Vehicle vehicle7 = vehicleOperations.findById(7L);
        var info = vehicle7.getVehicleInformation();
        String criteria = "brand";
        int resultSize = 1;
        String value = info.getBrand();
        performCriteriaRequest(criteria, resultSize, vehicle7, value, USER);
    }

    @Test
    @DisplayName("Find vehicles by registration number when role is ADMIN")
    void shouldFindVehiclesByCriteriaRegistrationNumber() throws Exception {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        String criteria = "registration number";
        int resultSize = 1;
        String value = vehicle8.getRegistrationNumber();
        performCriteriaRequest(criteria, resultSize, vehicle8, value, ADMIN);
    }

    @Test
    @DisplayName("Finding vehicles with role USER should throw exception")
    void shouldNotFindVehiclesByCriteriaRegistrationNumber() throws Exception {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        String criteria = "registration number";
        String value = vehicle8.getRegistrationNumber();
        performCriteriaRegistrationNumber(criteria, value);
    }

    @Test
    void shouldFindVehiclesByCriteriaProductionYear() throws Exception {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        var info = vehicle8.getVehicleInformation();
        String criteria = "production year";
        int resultSize = 2;
        String value = String.valueOf(info.getProductionYear().getYear());
        performCriteriaRequest(criteria, resultSize, vehicle8, value, USER);
    }

    @Test
    void shouldNotFindVehiclesByNonExistentCriteria() throws Exception {
        String userToken = tokenGenerator.generateToken(USER);
        String criteria = "wheels number";
        String value = "4";
        mockMvc.perform(get("/vehicles/criteria")
                        .param("criteriaName", criteria)
                        .param("value", value)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Unknown criteria type " + criteria));
    }

    @Test
    void shouldFindVehiclesByCriteriaStatusAvailable() throws Exception {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        vehicle8.setStatus(Vehicle.Status.UNAVAILABLE);
        vehicleRepository.save(vehicle8);

        String criteria = "status";
        int resultSize = 4;
        String value = "available";
        performCriteriaRequest(criteria, resultSize, vehicleWithId5, value, USER);
    }

    @Test
    void shouldFindVehiclesByCriteriaStatusUnavailable() throws Exception {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        vehicle8.setStatus(Vehicle.Status.UNAVAILABLE);
        vehicleRepository.save(vehicle8);

        String criteria = "status";
        int resultSize = 1;
        String value = "unavailable";
        performCriteriaRequest(criteria, resultSize, vehicle8, value, USER);
    }

    private void performCriteriaRequest(
            String criteria, int resultSize,
            Vehicle result, String value, String role)
            throws Exception {
        String userToken = tokenGenerator.generateToken(role);
        var info = result.getVehicleInformation();
        Year productionYear = info.getProductionYear();
        Engine engine = info.getEngine();
        String fuelType = engine.getFuelType().toString();
        String gearType = info.getGearType().toString();
        double pricePerDay = result.getPricePerDay()
                .getValue().setScale(2, RoundingMode.HALF_UP).doubleValue();
        String status = result.getStatus().toString();

        mockMvc.perform(get("/vehicles/criteria")
                        .param("criteriaName", criteria)
                        .param("value", value)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(resultSize)))
                .andExpect(jsonPath("$.[0].id").value(result.getId()))
                .andExpect(jsonPath("$.[0].vehicleInformation.brand").value(info.getBrand()))
                .andExpect(jsonPath("$.[0].vehicleInformation.model").value(info.getModel()))
                .andExpect(jsonPath("$.[0].vehicleInformation.registrationNumber").value(info.getRegistrationNumber()))
                .andExpect(jsonPath("$.[0].vehicleInformation.productionYear.year").value(productionYear.getYear()))
                .andExpect(jsonPath("$.[0].vehicleInformation.description").value(info.getDescription()))
                .andExpect(jsonPath("$.[0].vehicleInformation.engine.cylinders").value(engine.getCylinders()))
                .andExpect(jsonPath("$.[0].vehicleInformation.engine.engineType").value(engine.getEngineType()))
                .andExpect(jsonPath("$.[0].vehicleInformation.engine.fuelType").value(fuelType))
                .andExpect(jsonPath("$.[0].vehicleInformation.engine.displacement").value(engine.getDisplacement()))
                .andExpect(jsonPath("$.[0].vehicleInformation.engine.horsepower").value(engine.getHorsepower()))
                .andExpect(jsonPath("$.[0].vehicleInformation.gearType").value(gearType))
                .andExpect(jsonPath("$.[0].pricePerDay.value").value(pricePerDay))
                .andExpect(jsonPath("$.[0].status").value(status));
    }

    private void performCriteriaRegistrationNumber(
            String criteria, String value)
            throws Exception {
        String userToken = tokenGenerator.generateToken(USER);
        mockMvc.perform(get("/vehicles/criteria")
                        .param("criteriaName", criteria)
                        .param("value", value)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(
                        "Access denied: Only admins can search by registration number"
                ));
    }
}

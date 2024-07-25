package com.vehicle.sharing.zelezniak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.TokenGenerator;
import com.vehicle.sharing.zelezniak.config.VehicleCreator;
import com.vehicle.sharing.zelezniak.user_domain.service.authentication.JWTGenerator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.Engine;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.Year;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class VehicleControllerTest {

    private static Vehicle vehicleWithId5;
    private static Vehicle vehicleWithId6;
    private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private VehicleCreator vehicleCreator;
    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private TokenGenerator tokenGenerator;
    private String adminToken;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupVehicles();
        adminToken = tokenGenerator.generateToken(ADMIN);
        vehicleWithId5 = vehicleCreator.createCarWithId5();
        vehicleWithId6 = vehicleCreator.createMotorcycleWithId6();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupVehicles();
    }

    @Test
    void shouldReturnAllVehicles() throws Exception {
        String userToken = tokenGenerator.generateToken(USER);
        var info = vehicleWithId5.getVehicleInformation();
        Year productionYear = info.getProductionYear();
        Engine engine = info.getEngine();
        String fuelType = engine.getFuelType().toString();
        String gearType = info.getGearType().toString();
        double pricePerDay = vehicleWithId5.getPricePerDay()
                .getValue().setScale(2, RoundingMode.HALF_UP).doubleValue();
        String status = vehicleWithId5.getStatus().toString();

        mockMvc.perform(get("/vehicles/")
                        .header("Authorization", "Bearer " + userToken)
                        .param("page","0")
                        .param("size","5"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.content[0].id").value(vehicleWithId5.getId()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.model").value(info.getModel()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.brand").value(info.getBrand()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.registrationNumber.registration").value(info.getRegistrationNumber().getRegistration()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.productionYear.year").value(productionYear.getYear()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.description").value(info.getDescription()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.engine.cylinders").value(engine.getCylinders()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.engine.engineType").value(engine.getEngineType()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.engine.fuelType").value(fuelType))
                .andExpect(jsonPath("$.content[0].vehicleInformation.engine.displacement").value(engine.getDisplacement()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.engine.horsepower").value(engine.getHorsepower()))
                .andExpect(jsonPath("$.content[0].vehicleInformation.gearType").value(gearType))
                .andExpect(jsonPath("$.content[0].vehicleInformation.seatsNumber").value(info.getSeatsNumber()))
                .andExpect(jsonPath("$.content[0].pricePerDay.value").value(pricePerDay))
                .andExpect(jsonPath("$.content[0].status").value(status));
    }

    @Test
    void shouldFindVehicleById() throws Exception {
        var info = vehicleWithId5.getVehicleInformation();
        Year productionYear = info.getProductionYear();
        Engine engine = info.getEngine();
        String fuelType = engine.getFuelType().toString();
        String gearType = info.getGearType().toString();
        double pricePerDay = vehicleWithId5.getPricePerDay()
                .getValue().setScale(2, RoundingMode.HALF_UP).doubleValue();
        String status = vehicleWithId5.getStatus().toString();

        mockMvc.perform(get("/vehicles/{id}", vehicleWithId5.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleWithId5.getId()))
                .andExpect(jsonPath("$.vehicleInformation.brand").value(info.getBrand()))
                .andExpect(jsonPath("$.vehicleInformation.model").value(info.getModel()))
                .andExpect(jsonPath("$.vehicleInformation.registrationNumber.registration").value(info.getRegistrationNumber().getRegistration()))
                .andExpect(jsonPath("$.vehicleInformation.productionYear.year").value(productionYear.getYear()))
                .andExpect(jsonPath("$.vehicleInformation.description").value(info.getDescription()))
                .andExpect(jsonPath("$.vehicleInformation.engine.cylinders").value(engine.getCylinders()))
                .andExpect(jsonPath("$.vehicleInformation.engine.engineType").value(engine.getEngineType()))
                .andExpect(jsonPath("$.vehicleInformation.engine.fuelType").value(fuelType))
                .andExpect(jsonPath("$.vehicleInformation.engine.displacement").value(engine.getDisplacement()))
                .andExpect(jsonPath("$.vehicleInformation.engine.horsepower").value(engine.getHorsepower()))
                .andExpect(jsonPath("$.vehicleInformation.gearType").value(gearType))
                .andExpect(jsonPath("$.vehicleInformation.seatsNumber").value(info.getSeatsNumber()))
                .andExpect(jsonPath("$.pricePerDay.value").value(pricePerDay))
                .andExpect(jsonPath("$.status").value(status));
    }

    @Test
    void shouldNotFindVehicleById() throws Exception {
        Long nonExistentId = 20L;
        mockMvc.perform(get("/vehicles/{id}", nonExistentId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Vehicle with id: " + nonExistentId + " does not exists."));
    }

    @Test
    void shouldAddVehicle() throws Exception {
        Vehicle testCar = vehicleCreator.createTestCar();

        mockMvc.perform(post("/vehicles/add")
                        .content(objectMapper.writeValueAsString(testCar))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated());

        assertEquals(6, vehicleRepository.count());
        assertTrue(vehicleRepository.existsByVehicleInformationRegistrationNumber(
                testCar.getRegistrationNumber()));
    }

    @Test
    void shouldNotAddVehicle() throws Exception {
        RegistrationNumber n = vehicleWithId5.getRegistrationNumber();
        mockMvc.perform(post("/vehicles/add")
                        .content(objectMapper.writeValueAsString(vehicleWithId5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Vehicle with registration number : " + n.getRegistration() + " already exists"));
    }

    @Test
    void shouldUpdateVehicle() throws Exception {
        Long vehicle5Id = vehicleWithId5.getId();
        Vehicle newData = vehicleCreator.buildVehicle5WithDifferentData();

        mockMvc.perform(put("/vehicles/update/{id}", vehicle5Id)
                .content(objectMapper.writeValueAsString(newData))
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminToken)
        ).andExpect(status().isOk());

        Vehicle updated = vehicleService.findById(vehicle5Id);
        assertEquals(newData, updated);
    }

    @Test
    @DisplayName("Should not update vehicle when new data contains an existing registration number")
    void shouldNotUpdateVehicle() throws Exception {
        RegistrationNumber n = vehicleWithId6.getRegistrationNumber();
        Vehicle newData = vehicleCreator.buildVehicle5WithDifferentData();
        var vehicleInformation = newData.getVehicleInformation();
        var infoWithExistentRegistration = vehicleInformation.toBuilder()
                .registrationNumber(n)
                .build();
        newData.setVehicleInformation(infoWithExistentRegistration);
        Long vehicle5Id = vehicleWithId5.getId();

        mockMvc.perform(put("/vehicles/update/{id}", vehicle5Id)
                        .content(objectMapper.writeValueAsString(newData))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Vehicle with registration number : " + n.getRegistration() + " already exists"));
    }

    @Test
    void shouldDeleteVehicle() throws Exception {
        Long vehicle5Id = vehicleWithId5.getId();

        assertEquals(5, vehicleRepository.count());
        mockMvc.perform(delete("/vehicles/delete/{id}", vehicle5Id)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        assertEquals(4, vehicleRepository.count());

        Pageable pageable = PageRequest.of(0,5);
        Page<Vehicle> page = vehicleService.findAll(pageable);
        List<Vehicle> list = page.get().toList();
        assertFalse(list.contains(vehicleWithId5));
    }

    @Test
    void shouldNotDeleteVehicle() throws Exception {
        Long nonExistentId = 20L;

        assertEquals(5, vehicleRepository.count());
        mockMvc.perform(delete("/vehicles/delete/{id}", nonExistentId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Vehicle with id: " + nonExistentId + " does not exists."));

        assertEquals(5, vehicleRepository.count());
    }
}

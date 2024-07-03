package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Engine;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Money;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.VehicleInformation;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Year;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Car;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Motorcycle;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import com.car.sharing.zelezniak.sharing_domain.service.VehicleService;
import com.car.sharing.zelezniak.user_domain.service.authentication.JWTGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    private VehicleService vehicleOperations;

    @Autowired
    @Qualifier("car")
    private Vehicle car;

    @Autowired
    @Qualifier("motorcycle")
    private Vehicle motorcycle;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    String adminToken;

    @Value("${create.vehicle.five}")
    private String createVehicleFive;

    @Value("${create.car.five}")
    private String createCarFive;

    @Value("${create.vehicle.six}")
    private String createVehicleSix;

    @Value("${create.motorcycle.six}")
    private String createMotorcycleSix;

    @Value("${create.vehicle.seven}")
    private String createVehicleSeven;

    @Value("${create.car.seven}")
    private String createCarSeven;

    @Value("${create.vehicle.eight}")
    private String createVehicleEight;

    @Value("${create.car.eight}")
    private String createCarEight;

    @Value("${create.vehicle.nine}")
    private String createVehicleNine;

    @Value("${create.motorcycle.nine}")
    private String createMotorcycleNine;

    @Autowired
    private JWTGenerator jwtGenerator;

    @BeforeEach
    void setupDatabase() {
        executeQueries(createVehicleFive, createCarFive,
                createVehicleSix, createMotorcycleSix,
                createVehicleSeven, createCarSeven, createVehicleEight,
                createCarEight, createVehicleNine, createMotorcycleNine);
        adminToken = generateToken(ADMIN);
        vehicleWithId5 = createCarWithId5();
        vehicleWithId6 = createMotorcycleWithId6();
    }

    @AfterEach
    void cleanupDatabase() {
        executeQueries(
                "delete from cars",
                "delete from motorcycles",
                "delete from vehicle");
    }

    @Test
    void shouldReturnAllVehicles() throws Exception {
        String userToken = generateToken(USER);
        var info = vehicleWithId5.getVehicleInformation();
        Year productionYear = info.getProductionYear();
        Engine engine = info.getEngine();
        String fuelType = engine.getFuelType().toString();
        String gearType = info.getGearType().toString();
        double pricePerDay = vehicleWithId5.getPricePerDay()
                .getMoney().setScale(2, RoundingMode.HALF_UP).doubleValue();
        String status = vehicleWithId5.getStatus().toString();

        mockMvc.perform(get("/vehicles/")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$.[0].id").value(vehicleWithId5.getId()))
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
                .andExpect(jsonPath("$.[0].pricePerDay.money").value(pricePerDay))
                .andExpect(jsonPath("$.[0].status").value(status));
    }


    @Test
    void shouldFindVehicleById() throws Exception {
        var info = vehicleWithId5.getVehicleInformation();
        Year productionYear = info.getProductionYear();
        Engine engine = info.getEngine();
        String fuelType = engine.getFuelType().toString();
        String gearType = info.getGearType().toString();
        double pricePerDay = vehicleWithId5.getPricePerDay()
                .getMoney().setScale(2, RoundingMode.HALF_UP).doubleValue();
        String status = vehicleWithId5.getStatus().toString();

        mockMvc.perform(get("/vehicles/{id}", vehicleWithId5.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleWithId5.getId()))
                .andExpect(jsonPath("$.vehicleInformation.brand").value(info.getBrand()))
                .andExpect(jsonPath("$.vehicleInformation.model").value(info.getModel()))
                .andExpect(jsonPath("$.vehicleInformation.registrationNumber").value(info.getRegistrationNumber()))
                .andExpect(jsonPath("$.vehicleInformation.productionYear.year").value(productionYear.getYear()))
                .andExpect(jsonPath("$.vehicleInformation.description").value(info.getDescription()))
                .andExpect(jsonPath("$.vehicleInformation.engine.cylinders").value(engine.getCylinders()))
                .andExpect(jsonPath("$.vehicleInformation.engine.engineType").value(engine.getEngineType()))
                .andExpect(jsonPath("$.vehicleInformation.engine.fuelType").value(fuelType))
                .andExpect(jsonPath("$.vehicleInformation.engine.displacement").value(engine.getDisplacement()))
                .andExpect(jsonPath("$.vehicleInformation.engine.horsepower").value(engine.getHorsepower()))
                .andExpect(jsonPath("$.vehicleInformation.gearType").value(gearType))
                .andExpect(jsonPath("$.pricePerDay.money").value(pricePerDay))
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
        Vehicle testCar = createTestCar();

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
        String n = vehicleWithId5.getRegistrationNumber();
        mockMvc.perform(post("/vehicles/add")
                        .content(objectMapper.writeValueAsString(vehicleWithId5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Vehicle with registration number : " + n + " already exists"));
    }

    @Test
    void shouldUpdateVehicle() throws Exception {
        Long vehicle5Id = vehicleWithId5.getId();
        Vehicle newData = buildVehicle5WithDifferentData();

        mockMvc.perform(put("/vehicles/update/{id}", vehicle5Id)
                .content(objectMapper.writeValueAsString(newData))
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminToken)
        ).andExpect(status().isOk());

        Vehicle updated = vehicleOperations.findById(vehicle5Id);
        assertEquals(newData, updated);
    }

    @Test
    @DisplayName("Should not update vehicle when new data contains an existing registration number")
    void shouldNotUpdateVehicle() throws Exception {
        String n = vehicleWithId6.getRegistrationNumber();
        Vehicle newData = buildVehicle5WithDifferentData();
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
                        "Vehicle with registration number : " + n + " already exists"));
    }

    @Test
    void shouldDeleteVehicle() throws Exception {
        Long vehicle5Id = vehicleWithId5.getId();

        assertEquals(5, vehicleRepository.count());
        mockMvc.perform(delete("/vehicles/delete/{id}", vehicle5Id)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        assertEquals(4, vehicleRepository.count());
        Collection<Vehicle> all = vehicleOperations.findAll();
        assertFalse(all.contains(vehicleWithId5));
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

    @Test
    void shouldFindVehiclesByCriteriaModel() throws Exception {
        String criteria = "model";
        int resultSize = 1;
        String value = vehicleWithId5.getVehicleInformation().getModel();
        performCriteriaRequest(criteria, resultSize, vehicleWithId5, value);
    }

    @Test
    void shouldFindVehiclesByCriteriaBrand() throws Exception {
        Vehicle vehicle7 = vehicleOperations.findById(7L);
        var info = vehicle7.getVehicleInformation();
        String criteria = "brand";
        int resultSize = 1;
        String value = info.getBrand();
        performCriteriaRequest(criteria, resultSize, vehicle7, value);
    }

    @Test
    void shouldFindVehiclesByCriteriaRegistrationNumber() throws Exception {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        String criteria = "registration number";
        int resultSize = 1;
        String value = vehicle8.getRegistrationNumber();
        performCriteriaRequest(criteria, resultSize, vehicle8, value);
    }

    @Test
    void shouldFindVehiclesByCriteriaProductionYear() throws Exception {
        Vehicle vehicle8 = vehicleOperations.findById(8L);
        var info = vehicle8.getVehicleInformation();
        String criteria = "production year";
        int resultSize = 2;
        String value = String.valueOf(info.getProductionYear().getYear());
        performCriteriaRequest(criteria, resultSize, vehicle8, value);
    }

    @Test
    void shouldNotFindVehiclesByNonExistentCriteria() throws Exception {
        String userToken = generateToken(USER);
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

    private String generateToken(String role) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role));
        UserDetails userDetails = new User(role.toLowerCase(), "password", authorities);
        return jwtGenerator.generateJWT(new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities));
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries).forEach(jdbcTemplate::execute);
    }

    private static Vehicle createCarWithId5() {
        Engine engine = buildCarEngine();
        var information = buildCarInformation(engine);
        return Car.builder()
                .id(5L)
                .vehicleInformation(information)
                .bodyType(Car.BodyType.HATCHBACK)
                .status(Vehicle.Status.AVAILABLE)
                .driveType(Car.DriveType.FRONT_WHEEL_DRIVE)
                .pricePerDay(new Money(BigDecimal.valueOf(50.0)))
                .doorsNumber(5)
                .build();
    }

    private static Vehicle createMotorcycleWithId6() {
        Engine engine = buildMotorcycleEngine();
        var information = buildMotorcycleInformation(engine);
        return Motorcycle.builder()
                .id(6L)
                .vehicleInformation(information)
                .motorcycleType(Motorcycle.MotorcycleType.SPORT)
                .status(Vehicle.Status.AVAILABLE)
                .build();
    }

    private static Engine buildCarEngine() {
        return Engine.builder()
                .engineType("1.9TDI AVG")
                .fuelType(Engine.FuelType.DIESEL)
                .cylinders(4)
                .displacement(1900)
                .horsepower(110)
                .build();
    }

    private static Engine buildMotorcycleEngine() {
        return Engine.builder()
                .engineType("Minarelli-Yamaha 5D1E")
                .fuelType(Engine.FuelType.GASOLINE)
                .cylinders(1)
                .displacement(125)
                .horsepower(15)
                .build();
    }

    private static VehicleInformation buildCarInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Seat")
                .model("Leon 1M")
                .productionYear(new Year(2001))
                .registrationNumber("ABC55555")
                .description("Seat Leon car")
                .engine(engine)
                .gearType(VehicleInformation.GearType.MANUAL)
                .build();
    }

    private static VehicleInformation buildMotorcycleInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Yamaha")
                .model("YZF-R125")
                .productionYear(new Year(2015))
                .registrationNumber("ABC66666")
                .description("Legendary Yamaha 125")
                .engine(engine)
                .gearType(VehicleInformation.GearType.MANUAL)
                .build();
    }

    private Vehicle createTestCar() {
        Engine engine = buildTestCarEngine();
        VehicleInformation information = buildTestCarInformation(engine);
        return Car.builder()
                .vehicleInformation(information)
                .bodyType(Car.BodyType.COUPE)
                .status(Vehicle.Status.AVAILABLE)
                .driveType(Car.DriveType.FOUR_WHEEL_DRIVE)
                .pricePerDay(new Money(BigDecimal.valueOf(1000.0)))
                .doorsNumber(3)
                .build();
    }

    private Engine buildTestCarEngine() {
        return Engine.builder()
                .engineType("VR38DETT")
                .fuelType(Engine.FuelType.GASOLINE)
                .cylinders(6)
                .displacement(3800)
                .horsepower(565)
                .build();
    }

    private VehicleInformation buildTestCarInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Nissan")
                .model("GT-R R35")
                .productionYear(new Year(2021))
                .registrationNumber("GTR54321")
                .description("Nissan GT-R R35 high-performance sports car")
                .engine(engine)
                .gearType(VehicleInformation.GearType.AUTOMATIC)
                .build();
    }

    private Vehicle buildVehicle5WithDifferentData() {
        Engine engine = updateEngine();
        VehicleInformation information = updateInformation(engine);
        return Car.builder()
                .id(5L)
                .vehicleInformation(information)
                .bodyType(Car.BodyType.HATCHBACK)
                .status(Vehicle.Status.AVAILABLE)
                .driveType(Car.DriveType.FOUR_WHEEL_DRIVE)
                .pricePerDay(new Money(BigDecimal.valueOf(150.0)))
                .doorsNumber(5)
                .build();
    }

    private Engine updateEngine() {
        return Engine.builder()
                .engineType("1.9TDI AVG")
                .fuelType(Engine.FuelType.DIESEL)
                .cylinders(4)
                .displacement(1900)
                .horsepower(150)
                .build();
    }

    private VehicleInformation updateInformation(Engine engine) {
        return VehicleInformation.builder()
                .brand("Seat")
                .model("Leon 1M")
                .productionYear(new Year(2001))
                .registrationNumber("ABC55555")
                .description("Tuned Seat Leon")
                .engine(engine)
                .gearType(VehicleInformation.GearType.MANUAL)
                .build();
    }

    private void performCriteriaRequest(String criteria, int resultSize,
                                        Vehicle result, String value) throws Exception {
        String userToken = generateToken(USER);
        var info = result.getVehicleInformation();
        Year productionYear = info.getProductionYear();
        Engine engine = info.getEngine();
        String fuelType = engine.getFuelType().toString();
        String gearType = info.getGearType().toString();
        double pricePerDay = result.getPricePerDay()
                .getMoney().setScale(2, RoundingMode.HALF_UP).doubleValue();
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
                .andExpect(jsonPath("$.[0].pricePerDay.money").value(pricePerDay))
                .andExpect(jsonPath("$.[0].status").value(status));
    }
}

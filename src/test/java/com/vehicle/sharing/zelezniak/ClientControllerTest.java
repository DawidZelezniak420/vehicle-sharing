package com.vehicle.sharing.zelezniak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.sharing.zelezniak.common_value_objects.address.City;
import com.vehicle.sharing.zelezniak.common_value_objects.address.Country;
import com.vehicle.sharing.zelezniak.common_value_objects.address.Street;
import com.vehicle.sharing.zelezniak.config.ClientCreator;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.TokenGenerator;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Address;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Role;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserCredentials;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserName;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class ClientControllerTest {

    private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static Client clientWithId5;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientCreator clientCreator;
    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private TokenGenerator tokenGenerator;

    private String adminToken;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupClients();
        clientWithId5 = clientCreator.createClientWithId5();
        adminToken = tokenGenerator.generateToken(ADMIN);
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupClients();
    }

    @Test
    void shouldReturnAllClients() throws Exception {
        var credentials = clientWithId5.getCredentials();
        var name = clientWithId5.getName();
        mockMvc.perform(get("/clients/")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id").value(clientWithId5.getId()))
                .andExpect(jsonPath("$.[0].credentials.email").value(credentials.getEmail()))
                .andExpect(jsonPath("$.[0].credentials.password").value(credentials.getPassword()))
                .andExpect(jsonPath("$.[0].name.firstName").value(name.getFirstName()))
                .andExpect(jsonPath("$.[0].name.lastName").value(name.getLastName()))
                .andExpect(jsonPath("$.[0].address.street.streetName").value(clientWithId5.getAddress().getStreet().getStreetName()))
                .andExpect(jsonPath("$.[0].roles", hasSize(1)))
                .andExpect(jsonPath("$.[0].roles[0].roleName").value(USER));

    }

    @Test
    void shouldFindClientById() throws Exception {
        Long existingClientId = 5L;
        var credentials = clientWithId5.getCredentials();
        var name = clientWithId5.getName();
        mockMvc.perform(get("/clients/{id}", existingClientId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clientWithId5.getId()))
                .andExpect(jsonPath("$.credentials.email").value(credentials.getEmail()))
                .andExpect(jsonPath("$.credentials.password").value(credentials.getPassword()))
                .andExpect(jsonPath("$.name.firstName").value(name.getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(name.getLastName()))
                .andExpect(jsonPath("$.address.street.streetName").value(clientWithId5.getAddress().getStreet().getStreetName()))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0].roleName").value(USER));
    }

    @Test
    void shouldNotFindClientById() throws Exception {
        Long nonExistentId = 20L;
        mockMvc.perform(get("/clients/{id}", nonExistentId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "User with id: " + nonExistentId + " does not exist."))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Run update method with token generated for role USER")
    void shouldUpdateUserWhenRoleUser() throws Exception {
        String userToken = tokenGenerator.generateToken(USER);
        Client testData = createTestClient();

        performUpdateClient(testData, userToken);

        Client updated = clientService.findById(testData.getId());
        assertEquals(testData, updated);
    }

    @Test
    @DisplayName("Run update method with token generated for role ADMIN")
    void shouldUpdateUserWhenRoleAdmin() throws Exception {
        Client testData = createTestClient();

        performUpdateClient(testData, adminToken);

        Client updated = clientService.findById(testData.getId());
        assertEquals(testData, updated);
    }

    @Test
    void shouldDeleteClient() throws Exception {
        Long existingClientId = 5L;
        assertEquals(3, clientService.findAll().size());
        mockMvc.perform(delete("/clients/delete/{id}", existingClientId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
        assertEquals(2, clientService.findAll().size());
    }

    @Test
    void shouldFindClientByEmail() throws Exception {
        var credentials = clientWithId5.getCredentials();
        var name = clientWithId5.getName();
        mockMvc.perform(get("/clients/email/{email}", credentials.getEmail())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clientWithId5.getId()))
                .andExpect(jsonPath("$.credentials.email").value(credentials.getEmail()))
                .andExpect(jsonPath("$.credentials.password").value(credentials.getPassword()))
                .andExpect(jsonPath("$.name.firstName").value(name.getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(name.getLastName()))
                .andExpect(jsonPath("$.address.street.streetName").value(clientWithId5.getAddress().getStreet().getStreetName()))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0].roleName").value(USER));
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setId(5L);
        client.setName(new UserName("Jim", "Beam"));
        client.setCredentials(new UserCredentials(
                "someemail@gmail.com", "changedpass"));
        Address address = new Address(5L, new Street("somestreet"), "25",
                "10", new City("Lublin"), "21-070", new Country("Poland"));
        client.setAddress(address);
        client.setRoles(Set.of(new Role("USER")));
        return client;
    }

    private void performUpdateClient(
            Client newData, String token)
            throws Exception {
        Long existingClientId = 5L;
        mockMvc.perform(put("/clients/update/{id}", existingClientId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newData))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}

package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.user_domain.model.user.Address;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.model.user.Role;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.user_domain.service.ClientService;
import com.car.sharing.zelezniak.user_domain.service.authentication.JWTGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class ClientControllerTest {

    private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;
    private static Client clientWithId5;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientOperations;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClientService clientService;

    private String adminToken;

    @Value("${create.role.user}")
    private String createRoleUser;
    @Value("${create.user.five}")
    private String createUserFive;
    @Value("${create.user.six}")
    private String createUserSix;
    @Value("${create.user.seven}")
    private String createUserSeven;
    @Value("${set.role.user.five}")
    private String setRoleUserFive;
    @Value("${set.role.user.six}")
    private String setRoleUserSix;
    @Value("${set.role.user.seven}")
    private String setRoleUserSeven;
    @Value("${create.address.five}")
    private String createAddressFive;
    @Value("${create.address.six}")
    private String createAddressSix;
    @Value("${create.address.seven}")
    private String createAddressSeven;

    @BeforeEach
    void setupDatabase() {
        executeQueries(createRoleUser, createAddressFive, createAddressSix,
                createAddressSeven, createUserFive, createUserSix,
                createUserSeven, setRoleUserFive, setRoleUserSix, setRoleUserSeven);
        clientWithId5 = createClientWithId5();
        adminToken = generateToken("ADMIN");
    }

    @AfterEach
    void cleanupDatabase() {
        executeQueries("delete from clients_roles", "delete from roles",
                "delete from clients", "delete from addresses");
        clientWithId5 = null;
        adminToken = null;
    }

    @Test
    void shouldReturnAllClients() throws Exception {
        mockMvc.perform(get("/clients/")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id").value(clientWithId5.getId()))
                .andExpect(jsonPath("$.[0].credentials.email").value(clientWithId5.getCredentials().getEmail()))
                .andExpect(jsonPath("$.[0].credentials.password").value(clientWithId5.getCredentials().getPassword()))
                .andExpect(jsonPath("$.[0].name.firstName").value(clientWithId5.getName().getFirstName()))
                .andExpect(jsonPath("$.[0].name.lastName").value(clientWithId5.getName().getLastName()))
                .andExpect(jsonPath("$.[0].address.street").value(clientWithId5.getAddress().getStreet()))
                .andExpect(jsonPath("$.[0].roles", hasSize(1)))
                .andExpect(jsonPath("$.[0].roles[0].roleName").value("USER"));

    }

    @Test
    void shouldFindClientById() throws Exception {
        Long existingClientId = 5L;
        mockMvc.perform(get("/clients/{id}", existingClientId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clientWithId5.getId()))
                .andExpect(jsonPath("$.credentials.email").value(clientWithId5.getCredentials().getEmail()))
                .andExpect(jsonPath("$.credentials.password").value(clientWithId5.getCredentials().getPassword()))
                .andExpect(jsonPath("$.name.firstName").value(clientWithId5.getName().getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(clientWithId5.getName().getLastName()))
                .andExpect(jsonPath("$.address.street").value(clientWithId5.getAddress().getStreet()))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0].roleName").value("USER"));
    }

    @Test
    void shouldNotFindClientById() throws Exception {
        mockMvc.perform(get("/clients/{id}", 20L)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with id: 20 does not exist."))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Run update method with token generated for role USER")
    @Transactional
    void shouldUpdateUserWhenRoleUser() throws Exception {
        String userToken = generateToken("USER");
        Client testData = createTestClient();

        performUpdateClient(testData, userToken);

        Client updated = clientOperations.findById(testData.getId());
        assertEquals(testData, updated);
    }

    @Test
    @DisplayName("Run update method with token generated for role ADMIN")
    @Transactional
    void shouldUpdateUserWhenRoleAdmin() throws Exception {
        Client testData = createTestClient();

        performUpdateClient(testData, adminToken);

        Client updated = clientOperations.findById(testData.getId());
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

    private String generateToken(String role) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role));
        UserDetails userDetails = new User(role.toLowerCase(), "password", authorities);
        return jwtGenerator.generateJWT(new UsernamePasswordAuthenticationToken(userDetails, null, authorities));
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setId(5L);
        client.setName(new UserName("Jim", "Beam"));
        client.setCredentials(new UserCredentials("someemail@gmail.com", "changedpass"));
        Address address = new Address(5L, "somestreet", "25", "10", "Lublin", "21-070", "Poland");
        client.setAddress(address);
        client.setRoles(Set.of(new Role("USER")));
        return client;
    }

    private void performUpdateClient(Client newData, String token) throws Exception {
        Long existingClientId = 5L;
        mockMvc.perform(put("/clients/update/{id}", existingClientId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newData))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries).forEach(jdbcTemplate::execute);
    }

    private static Client createClientWithId5() {
        Client client = new Client();
        client.setId(5L);
        client.setName(new UserName("UserFive", "Five"));
        client.setCredentials(new UserCredentials("userfive@gmail.com", "somepass"));
        Address address = new Address(5L, "teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        client.setAddress(address);
        client.setRoles(Set.of(new Role("USER")));
        return client;
    }
}

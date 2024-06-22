package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.userdomain.controller.ClientController;
import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.Client;
import com.car.sharing.zelezniak.userdomain.model.user.Role;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.service.ClientService;
import com.car.sharing.zelezniak.userdomain.service.authentication.JWTGenerator;
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

import java.util.HashSet;
import java.util.List;
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
    private static Client userWithId5;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientController clientController;

    @Autowired
    private Client client;

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

    private String token;

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
    void createUsers() {
        jdbcTemplate.execute(createRoleUser);
        jdbcTemplate.execute(createAddressFive);
        jdbcTemplate.execute(createAddressSix);
        jdbcTemplate.execute(createAddressSeven);
        jdbcTemplate.execute(createUserFive);
        jdbcTemplate.execute(createUserSix);
        jdbcTemplate.execute(createUserSeven);
        jdbcTemplate.execute(setRoleUserFive);
        jdbcTemplate.execute(setRoleUserSix);
        jdbcTemplate.execute(setRoleUserSeven);
        userWithId5 = createUserWithId5();
        token = generateToken();
    }

    @Test
    void shouldReturnAllUsersWithCorrectData() throws Exception {
        mockMvc.perform(get("/clients/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id").value(userWithId5.getId()))
                .andExpect(jsonPath("$.[0].credentials.email").value(userWithId5.getEmail()))
                .andExpect(jsonPath("$.[0].credentials.password").value(userWithId5.getPassword()))
                .andExpect(jsonPath("$.[0].name.firstName").value(userWithId5.getName().getFirstName()))
                .andExpect(jsonPath("$.[0].name.lastName").value(userWithId5.getName().getLastName()))
                .andExpect(jsonPath("$.[0].address").value(userWithId5.getAddress()));
    }

    @Test
    void shouldFindAppUserById() throws Exception {
        mockMvc.perform(get("/clients/{id}", 5L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.id").value(userWithId5.getId()))
                .andExpect(jsonPath("$.credentials.email").value(userWithId5.getEmail()))
                .andExpect(jsonPath("$.credentials.password").value(userWithId5.getPassword()))
                .andExpect(jsonPath("$.name.firstName").value(userWithId5.getName().getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(userWithId5.getName().getLastName()))
                .andExpect(jsonPath("$.address").value(userWithId5.getAddress()));
    }

    @Test
    void shouldNotFindAppUserById() throws Exception {
        mockMvc.perform(get("/clients/{id}", 20L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("User with id : " + 20 + " does not exists."))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("run update method with token generated for role ADMIN")
    @Transactional
    void shouldUpdateUserWhenRoleAdmin() throws Exception {
        client.setName(new UserName("Jim", "Beam"));
        client.setCredentials(new UserCredentials("someemail@gmail.com", "changedpass"));
        Address address = new Address(5L, "somestreet", "25", "10", "Lublin", "21-070", "Poland");
        entityManager.merge(address);
        client.setAddress(address);

        mockMvc.perform(put("/clients/update/{id}", 5L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client))
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userWithId5.getId()))
                .andExpect(jsonPath("$.credentials.email").value(client.getEmail()))
                .andExpect(jsonPath("$.credentials.password").value(client.getPassword()))
                .andExpect(jsonPath("$.name.firstName").value(client.getName().getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(client.getName().getLastName()))
                .andExpect(jsonPath("$.address").value(client.getAddress()));
    }

    @Test
    @DisplayName("run update method with token generated for role USER")
    @Transactional
    void shouldUpdateUserWhenRoleUser() throws Exception {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        UserDetails userDetails = new User("user", "password", authorities);
        String token = jwtGenerator.generateJWT(new UsernamePasswordAuthenticationToken(userDetails, null, authorities));

        client.setName(new UserName("Jim", "Beam"));
        client.setCredentials(new UserCredentials("someemail@gmail.com", "changedpass"));
        Address address = new Address(5L, "somestreet", "25", "10", "Lublin", "21-070", "Poland");
        entityManager.merge(address);
        client.setAddress(address);

        mockMvc.perform(put("/clients/update/{id}", 5L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client))
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userWithId5.getId()))
                .andExpect(jsonPath("$.credentials.email").value(client.getEmail()))
                .andExpect(jsonPath("$.credentials.password").value(client.getPassword()))
                .andExpect(jsonPath("$.name.firstName").value(client.getName().getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(client.getName().getLastName()))
                .andExpect(jsonPath("$.address").value(client.getAddress()));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        assertEquals(3, clientService.getAll().size());
        mockMvc.perform(delete("/clients/delete/{id}", 5L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
        assertEquals(2, clientService.getAll().size());
    }

    @AfterEach
    void cleanupDatabase() {
        jdbcTemplate.execute("delete from clients_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from clients");
        jdbcTemplate.execute("delete from addresses");
        userWithId5 = null;
        token = null;
    }

    private String generateToken() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetails userDetails = new User("admin", "password", authorities);
        return jwtGenerator.generateJWT(new UsernamePasswordAuthenticationToken(userDetails, null, authorities));
    }

    private static Client createUserWithId5() {
        Client user = new Client();
        user.setId(5L);
        user.setName(new UserName("UserFive", "Five"));
        user.setCredentials(new UserCredentials("userfive@gmail.com", "somepass"));
        Address address = new Address(5L, "teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        user.setAddress(address);
        user.setRoles(
                new HashSet<>(
                        List.of(new Role("USER"))));
        return user;
    }
}

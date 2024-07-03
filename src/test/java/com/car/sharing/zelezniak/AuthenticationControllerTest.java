package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.user_domain.model.login.LoginRequest;
import com.car.sharing.zelezniak.user_domain.model.user.Address;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.car.sharing.zelezniak.user_domain.service.authentication.AuthenticationService;
import com.car.sharing.zelezniak.util.TimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = CarSharingApplication.class)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    private static final MediaType APPLICATION_JSON =
            MediaType.APPLICATION_JSON;

    @Autowired
    private Client client;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void createUser() {
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));
        client.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address(null, "teststreet", "5",
                "150", "Warsaw", "00-001", "Poland");
        client.setAddress(address);
    }

    @AfterEach
    void deleteTestData() {
        jdbcTemplate.execute("delete from clients_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from clients");
        jdbcTemplate.execute("delete from addresses");
    }

    @Test
    void shouldRegisterUser() throws Exception {
        UserName name = client.getName();
        Address address = client.getAddress();
        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name.firstName").value(name.getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(name.getLastName()))
                .andExpect(jsonPath("$.credentials.email").value(client.getEmail()))
                .andExpect(jsonPath("$.address.street").value(address.getStreet()))
                .andExpect(jsonPath("$.address.houseNumber").value(address.getHouseNumber()))
                .andExpect(jsonPath("$.address.flatNumber").value(address.getFlatNumber()))
                .andExpect(jsonPath("$.address.city").value(address.getCity()))
                .andExpect(jsonPath("$.address.postalCode").value(address.getPostalCode()))
                .andExpect(jsonPath("$.address.country").value(address.getCountry()));

        assertEquals(1, clientRepository.count());
    }

    @Test
    void shouldLoginUser() throws Exception {
        authService.register(client);
        String email = client.getEmail();
        LoginRequest loginRequest = new LoginRequest(
               email , "somepassword");

        Client registeredUser = clientRepository.findByCredentialsEmail(
                email);
        UserName name = registeredUser.getName();
        Address address = registeredUser.getAddress();

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.client.name.firstName").value(name.getFirstName()))
                .andExpect(jsonPath("$.client.name.lastName").value(name.getLastName()))
                .andExpect(jsonPath("$.client.credentials.email").value(client.getEmail()))
                .andExpect(jsonPath("$.client.address.street").value(address.getStreet()))
                .andExpect(jsonPath("$.client.address.houseNumber").value(address.getHouseNumber()))
                .andExpect(jsonPath("$.client.address.flatNumber").value(address.getFlatNumber()))
                .andExpect(jsonPath("$.client.address.city").value(address.getCity()))
                .andExpect(jsonPath("$.client.address.postalCode").value(address.getPostalCode()))
                .andExpect(jsonPath("$.client.address.country").value(address.getCountry()))
                .andExpect(jsonPath("$.jwt").isNotEmpty());
    }


}

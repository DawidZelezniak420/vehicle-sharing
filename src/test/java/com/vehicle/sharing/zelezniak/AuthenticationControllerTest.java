package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.user_domain.model.login.LoginRequest;
import com.vehicle.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.vehicle.sharing.zelezniak.user_domain.service.authentication.AuthenticationService;
import com.vehicle.sharing.zelezniak.util.TimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.sharing.zelezniak.user_domain.model.client.*;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserCredentials;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserName;
import com.vehicle.sharing.zelezniak.common_value_objects.City;
import com.vehicle.sharing.zelezniak.common_value_objects.Country;
import com.vehicle.sharing.zelezniak.common_value_objects.Street;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = CarSharingApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class AuthenticationControllerTest {

    private static final MediaType APPLICATION_JSON =
            MediaType.APPLICATION_JSON;

    @Autowired
    private Client client;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DatabaseSetup databaseSetup;

    @BeforeEach
    void createUser() {
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));
        client.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address(null, new Street("teststreet"), "5",
                "150", new City("Warsaw"), "00-001", new Country("Poland"));
        client.setAddress(address);
    }

    @AfterEach
    void deleteTestData() {
        databaseSetup.cleanupClients();
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
                .andExpect(jsonPath("$.address.street.streetName").value(address.getStreet().getStreetName()))
                .andExpect(jsonPath("$.address.houseNumber").value(address.getHouseNumber()))
                .andExpect(jsonPath("$.address.flatNumber").value(address.getFlatNumber()))
                .andExpect(jsonPath("$.address.city.cityName").value(address.getCity().getCityName()))
                .andExpect(jsonPath("$.address.postalCode").value(address.getPostalCode()))
                .andExpect(jsonPath("$.address.country.countryName").value(address.getCountry().getCountryName()));

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
                .andExpect(jsonPath("$.client.address.street.streetName").value(address.getStreet().getStreetName()))
                .andExpect(jsonPath("$.client.address.houseNumber").value(address.getHouseNumber()))
                .andExpect(jsonPath("$.client.address.flatNumber").value(address.getFlatNumber()))
                .andExpect(jsonPath("$.client.address.city.cityName").value(address.getCity().getCityName()))
                .andExpect(jsonPath("$.client.address.postalCode").value(address.getPostalCode()))
                .andExpect(jsonPath("$.client.address.country.countryName").value(address.getCountry().getCountryName()))
                .andExpect(jsonPath("$.jwt").isNotEmpty());
    }

    @Test
    void shouldTestInvalidEmailPattern() throws Exception {
        client.setCredentials(new UserCredentials(
                "wrongemail@com","somepassword"));
        String email = client.getEmail();
        mockMvc.perform(post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(jsonPath("$.message").value(
                        "Email " + email + " has invalid pattern."));
    }
}

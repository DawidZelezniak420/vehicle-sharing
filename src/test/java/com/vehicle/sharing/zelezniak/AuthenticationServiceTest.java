package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.common_value_objects.address.City;
import com.vehicle.sharing.zelezniak.common_value_objects.address.Country;
import com.vehicle.sharing.zelezniak.common_value_objects.address.Street;
import com.vehicle.sharing.zelezniak.config.ClientCreator;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Address;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserCredentials;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserName;
import com.vehicle.sharing.zelezniak.user_domain.model.login.LoginRequest;
import com.vehicle.sharing.zelezniak.user_domain.model.login.LoginResponse;
import com.vehicle.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import com.vehicle.sharing.zelezniak.user_domain.service.authentication.AuthenticationService;
import com.vehicle.sharing.zelezniak.util.TimeFormatter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationServiceTest {

    private static Client clientWithId5;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private Client client;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private ClientCreator clientCreator;

    @BeforeEach
    void createUsers() {
        databaseSetup.setupClients();
        clientWithId5 = clientCreator.createClientWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupClients();
        client = new Client();
    }

    @Test
    @Order(1)
    void shouldRegisterNewUser() {
        setDataForClient();
        authenticationService.register(client);

        assertEquals(4, clientRepository.count());
        Assertions.assertEquals(client, clientService.findById(client.getId()));
    }

    @Test
    @Order(2)
    void shouldLoginUser() {
        setDataForClient();
        authenticationService.register(client);

        LoginRequest loginRequest = new LoginRequest(
                client.getEmail(), "somepassword");
        LoginResponse login = authenticationService.login(loginRequest);

        Assertions.assertEquals(client, login.getClient());
        String token = login.getJwt();
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length);
    }

    @Test
    void shouldLoadUserByEmail() {
        assertEquals(clientWithId5,
                authenticationService.loadUserByUsername(
                                    clientWithId5.getEmail()));
    }

    private void setDataForClient() {
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials(
                "bob@gmail.com", "somepassword"));
        client.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address(null, new Street("teststreet"),
                "5", "150", new City("Warsaw"),
                "00-001", new Country("Poland"));
        client.setAddress(address);
    }
}

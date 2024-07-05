package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.config.*;
import com.car.sharing.zelezniak.user_domain.model.login.*;
import com.car.sharing.zelezniak.user_domain.model.user.*;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.*;
import com.car.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.car.sharing.zelezniak.user_domain.service.ClientService;
import com.car.sharing.zelezniak.user_domain.service.authentication.AuthenticationService;
import com.car.sharing.zelezniak.util.TimeFormatter;
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
        assertEquals(client, clientService.findById(client.getId()));
    }

    @Test
    @Order(2)
    void shouldLoginUser() {
        setDataForClient();
        authenticationService.register(client);

        LoginRequest loginRequest = new LoginRequest(
                client.getEmail(), "somepassword");
        LoginResponse login = authenticationService.login(loginRequest);

        assertEquals(client, login.getClient());
        String token = login.getJwt();
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length);
    }

    @Test
    void shouldLoadUserByEmail() {
        assertEquals(clientWithId5,
                authenticationService.loadUserByUsername(
                                    "userfive@gmail.com"));
    }

    private void setDataForClient() {
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials(
                "bob@gmail.com", "somepassword"));
        client.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address(null, "teststreet",
                "5", "150", "Warsaw",
                "00-001", "Poland");
        client.setAddress(address);
    }
}

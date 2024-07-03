package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.user_domain.model.login.LoginRequest;
import com.car.sharing.zelezniak.user_domain.model.login.LoginResponse;
import com.car.sharing.zelezniak.user_domain.model.user.Address;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.car.sharing.zelezniak.user_domain.service.ClientService;
import com.car.sharing.zelezniak.user_domain.service.authentication.AuthenticationService;
import com.car.sharing.zelezniak.util.TimeFormatter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationServiceTest {

    private static Client userWithId5;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private Client client;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ClientService clientOperations;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        executeQueries(createRoleUser, createAddressFive,
                createAddressSix, createAddressSeven, createUserFive,
                createUserSix, createUserSeven, setRoleUserFive,
                setRoleUserSix, setRoleUserSeven);
        userWithId5 = createUserWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        executeQueries("delete from clients_roles", "delete from roles",
                "delete from clients", "delete from addresses");
        userWithId5 = null;
        client = new Client();
    }

    @Test
    @Transactional
    void shouldRegisterNewUser() {
        setClientInfo();

        authenticationService.register(client);

        assertEquals(4, clientRepository.count());
        assertEquals(client, clientOperations.findById(client.getId()));
    }

    @Test
    @Transactional
    void shouldRegisterAndLoginUser() {
        setClientInfo();

        authenticationService.register(client);
        LoginRequest loginRequest = new LoginRequest("bob@gmail.com", "somepassword");
        LoginResponse login = authenticationService.login(loginRequest);

        assertEquals(client, login.getClient());
        String token = login.getJwt();
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length);
    }

    @Test
    void shouldLoadUserByEmail() {
        assertEquals(userWithId5, clientRepository.findByCredentialsEmail("userfive@gmail.com"));
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries).forEach(jdbcTemplate::execute);
    }

    private static Client createUserWithId5() {
        Client user = new Client();
        user.setId(5L);
        user.setName(new UserName("UserFive", "Five"));
        user.setCredentials(new UserCredentials("userfive@gmail.com", "somepass"));
        Address address = new Address(5L, "teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        user.setAddress(address);
        return user;
    }

    private void setClientInfo() {
        client.setId(null);
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));
        client.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address(null, "teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        client.setAddress(address);
    }
}

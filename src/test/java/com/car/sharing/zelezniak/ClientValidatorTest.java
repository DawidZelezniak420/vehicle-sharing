package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.config.*;
import com.car.sharing.zelezniak.user_domain.model.user.*;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.user_domain.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class ClientValidatorTest {

    private static Client clientWithId5;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ClientValidator validator;
    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private ClientCreator clientCreator;
    @Autowired
    private ClientService clientService;

    @BeforeEach
    void createUsers() {
        databaseSetup.setupClients();
        clientWithId5 = clientCreator.createClientWithId5();
    }

    @AfterEach
    void deleteDataFromDb() {
        databaseSetup.cleanupClients();
    }

    @Test
    void shouldThrowExceptionIfClientExists() {
        String existingEmail = clientWithId5.getEmail();

        assertThrows(IllegalArgumentException.class, () ->
                validator.ifUserExistsThrowException(existingEmail));
    }

    @Test
    void shouldNotThrowExceptionIfClientNotExists() {
        Client c = new Client();
        c.setCredentials(new UserCredentials(
                "someuser@gmail.com", "somepass"));

        assertDoesNotThrow(() -> validator.ifUserExistsThrowException(
                c.getEmail()));
    }

    @Test
    void shouldTestClientCanBeUpdated() {
        String userFromDbEmail = clientWithId5.getEmail();
        clientWithId5.setCredentials(new UserCredentials(
                "newemail@gmail.com", "somepass"));

        assertDoesNotThrow(() ->
                validator.checkIfUserCanBeUpdated(
                        userFromDbEmail, clientWithId5));
    }

    @Test
    void shouldTestClientCanNotBeUpdated() {
        Client byId = clientService.findById(6L);
        String existingEmail = byId.getEmail();

        assertThrows(IllegalArgumentException.class, () ->
                validator.checkIfUserCanBeUpdated(
                        existingEmail, clientWithId5));
    }
}

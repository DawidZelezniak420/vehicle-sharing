package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.config.TestBeans;
import com.car.sharing.zelezniak.user_domain.model.user.Address;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.user_domain.service.ClientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@Import(TestBeans.class)
class ClientServiceTest {
    private static Client clientWithId5;

    @Autowired
    private Client client;

    @Autowired
    private ClientService clientOperations;

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
        clientWithId5 = createClientWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        executeQueries("delete from clients_roles", "delete from roles",
                "delete from clients", "delete from addresses");
        clientWithId5 = null;
        client = new Client();
    }

    @Test
    void shouldReturnAllClients() {
        List<Client> clients = clientOperations.findAll();
        assertTrue(clients.contains(clientWithId5));
        assertEquals(3, clients.size());

        for (Client client : clients) {
            assertNotNull(client.getId());
            assertNotNull(client.getUsername());
            assertNotNull(client.getCredentials());
            assertNotNull(client.getAddress());
        }
    }

    @Test
    void shouldFindClientById() {
        Client client = clientOperations.findById(
                clientWithId5.getId());
        assertEquals(clientWithId5, client);
    }

    @Test
    void shouldNotFindClientById() {
        Long nonExistentId = 20L;
        assertThrows(NoSuchElementException.class, () ->
                clientOperations.findById(nonExistentId));
    }

    @Test
    void shouldUpdateClient() {
        Long client5Id = clientWithId5.getId();
        client.setId(client5Id);
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));

        clientOperations.update(client5Id, client);
        Client updatedUser = clientOperations.findById(client5Id);

        assertEquals(client, updatedUser);
    }

    @Test
    void shouldNotUpdateClient() {
        client.setName(new UserName("Uncle", "Bob"));
        var credentialsWithExistingEmail = new UserCredentials("usersix@gmail.com", "somepassword");
        client.setCredentials(credentialsWithExistingEmail);

        Long client5Id = clientWithId5.getId();
        assertThrows(IllegalArgumentException.class, () ->
                clientOperations.update(
                        client5Id, client));
    }

    @Test
    void shouldDeleteClient() {
        assertEquals(3, clientOperations.findAll().size());
        clientOperations.delete(clientWithId5.getId());
        List<Client> clients = clientOperations.findAll();
        assertEquals(2, clients.size());
        assertFalse(clients.contains(clientWithId5));
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
        return client;
    }
}

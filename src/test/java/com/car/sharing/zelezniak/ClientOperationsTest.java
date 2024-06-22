package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.config.TestBeans;
import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.Client;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.service.ClientOperations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@Import(TestBeans.class)
class ClientOperationsTest {

    private static Client userWithId5;

    @Autowired
    private Client client;

    @Autowired
    private ClientOperations clientOperations;

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
    }

    @Test
    void shouldReturnAllUsersWithCorrectData() {
        List<Client> clients = clientOperations.getAll();
        assertTrue(clients.contains(userWithId5));
        assertEquals(3, clients.size());

        for (Client user : clients) {
            assertNotNull(user.getId());
            assertNotNull(user.getUsername());
            assertNotNull(user.getCredentials());
            assertNotNull(user.getAddress());
        }
    }

    @Test
    void shouldFindAppUserById() {
        Client client = clientOperations.getById(5L);
        assertEquals(userWithId5, client);
    }

    @Test
    void shouldNotFindAppUserById(){
        Long nonExistentId= 20L;
        assertThrows(NoSuchElementException.class,()->
                clientOperations.getById(nonExistentId));
    }

    @Test
    void shouldUpdateUser() {
        client.setId(5L);
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));

        clientOperations.update(5L, client);
        Client updatedUser = clientOperations.getById(5L);

        assertEquals(client, updatedUser);
    }

    @Test
    void shouldNotUpdateUser() {
        client.setId(5L);
        client.setName(new UserName("Uncle", "Bob"));
        client.setCredentials(new UserCredentials("usersix@gmail.com", "somepassword"));

        assertThrows(IllegalArgumentException.class,()->
                clientOperations.update(5L, client));
    }

    @Test
    void shouldDeleteUser() {
        assertEquals(3, clientOperations.getAll().size());
        clientOperations.delete(5L);
        List<Client> clients = clientOperations.getAll();
        assertEquals(2, clients.size());
        assertFalse(clients.contains(userWithId5));
    }

    @AfterEach
    void cleanupDatabase() {
        jdbcTemplate.execute("delete from clients_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from clients");
        jdbcTemplate.execute("delete from addresses");
        userWithId5 = null;
        client = new Client();
    }

    private static Client createUserWithId5() {
        Client user = new Client();
        user.setId(5L);
        user.setName(new UserName("UserFive", "Five"));
        user.setCredentials(new UserCredentials("userfive@gmail.com", "somepass"));
        Address address = new Address(5L,"teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        user.setAddress(address);
        return user;
    }
}

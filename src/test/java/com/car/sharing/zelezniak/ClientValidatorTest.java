package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.user_domain.model.user.Address;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.user_domain.service.UserValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class ClientValidatorTest {

    private static Client userWithId5;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserValidator validator;

    @Value("${create.role.user}")
    private String createRoleUser;
    @Value("${create.user.five}")
    private String createUserFive;
    @Value("${create.user.six}")
    private String createUserSix;
    @Value("${set.role.user.five}")
    private String setRoleUserFive;
    @Value("${set.role.user.six}")
    private String setRoleUserSix;
    @Value("${create.address.five}")
    private String createAddressFive;
    @Value("${create.address.six}")
    private String createAddressSix;

    @BeforeEach
    void createUsers(){
        executeQueries(createRoleUser,createAddressFive,
                createAddressSix,createUserFive,createUserSix,
                setRoleUserFive,setRoleUserSix);
        userWithId5 = createClientWithId5();
    }

    @AfterEach
    void deleteDataFromDb(){
        executeQueries("delete from clients_roles","delete from roles",
                "delete from clients","delete from addresses");
        userWithId5 = null;
    }

    @Test
    void shouldThrowException() {
        String given = null;
        assertThrows(IllegalArgumentException.class, () ->
                validator.throwExceptionIfObjectIsNull(given));
    }

    @Test
    void shouldNotThrowException() {
        String given = "string";
        assertDoesNotThrow(() ->
                validator.throwExceptionIfObjectIsNull(given));
    }

    @Test
    void shouldThrowExceptionIfClientExists() {
        String existingUserEmail = userWithId5.getEmail();
        assertThrows(IllegalArgumentException.class,()->
           validator.ifUserExistsThrowException(existingUserEmail));
    }

    @Test
    void shouldNotThrowExceptionIfClientNotExists() {
        Client c = new Client();
        c.setCredentials(new UserCredentials("someuser@gmail.com", "somepass"));

        assertDoesNotThrow(() -> validator.ifUserExistsThrowException(c.getEmail()));
    }

    @Test
    void shouldTestClientCanBeUpdated(){
        String userFromDbEmail = userWithId5.getEmail();
        userWithId5.setCredentials(new UserCredentials("newemail@gmail.com","somepass"));
        assertDoesNotThrow(()->
                validator.checkIfUserCanBeUpdated(userFromDbEmail,userWithId5));
    }

    @Test
    void shouldTestClientCanNotBeUpdated(){
        String userFromDbEmail = "usersix@gmail.com";
        assertThrows(IllegalArgumentException.class,()->
                validator.checkIfUserCanBeUpdated(userFromDbEmail,userWithId5));
    }

    private void executeQueries(String...queries) {
        Arrays.stream(queries).forEach(jdbcTemplate::execute);
    }


    private static Client createClientWithId5() {
        Client client = new Client();
        client.setId(5L);
        client.setName(new UserName("UserFive", "Five"));
        client.setCredentials(new UserCredentials("userfive@gmail.com", "somepass"));
        Address address = new Address(5L,"teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        client.setAddress(address);
        return client;
    }
}

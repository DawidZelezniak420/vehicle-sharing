package com.CarSharing.userdomain;

import com.car.sharing.zelezniak.CarSharingApplication;
import com.car.sharing.zelezniak.userdomain.model.Address;
import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.service.UserOperationsValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class UserOperationsValidatorTest {

    private static ApplicationUser userWithId5;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserOperationsValidator validator;

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
    @Value("${create.address.three}")
    private String createAddressThree;

    @BeforeEach
    void createUsers(){
        jdbcTemplate.execute(createRoleUser);
        jdbcTemplate.execute(createAddressThree);
        jdbcTemplate.execute(createUserFive);
        jdbcTemplate.execute(createUserSix);
        jdbcTemplate.execute(setRoleUserFive);
        jdbcTemplate.execute(setRoleUserSix);
        userWithId5 = createUserWithId5();
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
    void shouldTestUserExists() {
        String existingUserEmail = userWithId5.getEmail();
        assertThrows(IllegalArgumentException.class,()->
           validator.checkIfUserExists(existingUserEmail));
    }

    @Test
    void shouldTestUserNotExists() {
        ApplicationUser user = new ApplicationUser();
        user.setCredentials(new UserCredentials("someuser@gmail.com", "somepass"));

        assertDoesNotThrow(() -> validator.checkIfUserExists(user.getEmail()));
    }

    @Test
    void shouldTestUserCanBeUpdated(){
        String userFromDbEmail = userWithId5.getEmail();
        userWithId5.setCredentials(new UserCredentials("newemail@gmail.com","somepass"));
        assertDoesNotThrow(()->
                validator.checkIfUserCanBeUpdated(userFromDbEmail,userWithId5));
    }

    @Test
    void shouldTestUserCanNotBeUpdated(){
        String userFromDbEmail = "usersix@gmail.com";
        assertThrows(IllegalArgumentException.class,()->
                validator.checkIfUserCanBeUpdated(userFromDbEmail,userWithId5));
    }


    @AfterEach
    void deleteDataFromDb(){
        jdbcTemplate.execute("delete from users_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from users");
        jdbcTemplate.execute("delete from addresses");
        userWithId5 = null;
    }

    private static ApplicationUser createUserWithId5() {
        ApplicationUser user = new ApplicationUser();
        user.setId(5L);
        user.setName(new UserName("UserFive", "Five"));
        user.setCredentials(new UserCredentials("userfive@gmail.com", "somepass"));
        Set<ApplicationUser> users = new HashSet<>();
        users.add(user);
        Address address = new Address(3L, "teststreet", "5", "150", "Warsaw", "00-001", "Poland", users);
        user.setAddress(address);
        return user;
    }
}

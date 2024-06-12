package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.service.UserValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class UserValidatorTest {

    private static ApplicationUser userWithId5;

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
        jdbcTemplate.execute(createRoleUser);
        jdbcTemplate.execute(createAddressFive);
        jdbcTemplate.execute(createAddressSix);
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
    void shouldThrowExceptionIfUserExists() {
        String existingUserEmail = userWithId5.getEmail();
        assertThrows(IllegalArgumentException.class,()->
           validator.ifUserExistsThrowException(existingUserEmail));
    }

    @Test
    void shouldNotThrowExceptionIfUserNotExists() {
        ApplicationUser user = new ApplicationUser();
        user.setCredentials(new UserCredentials("someuser@gmail.com", "somepass"));

        assertDoesNotThrow(() -> validator.ifUserExistsThrowException(user.getEmail()));
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
        Address address = new Address(5L,"teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        user.setAddress(address);
        return user;
    }
}

package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.config.TestBeans;
import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.service.UserOperations;
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
class UserOperationsTest {

    private static ApplicationUser userWithId5;

    @Autowired
    private ApplicationUser appUser;

    @Autowired
    private UserOperations userOperations;

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
        List<ApplicationUser> allUsers = userOperations.getAll();
        assertTrue(allUsers.contains(userWithId5));
        assertEquals(3, allUsers.size());

        for (ApplicationUser user : allUsers) {
            assertNotNull(user.getId());
            assertNotNull(user.getUsername());
            assertNotNull(user.getCredentials());
            assertNotNull(user.getAddress());
        }
    }

    @Test
    void shouldFindAppUserById() {
        ApplicationUser user = userOperations.getById(5L);
        assertEquals(userWithId5, user);
    }

    @Test
    void shouldNotFindAppUserById(){
        Long nonExistentId= 20L;
        assertThrows(NoSuchElementException.class,()->
                userOperations.getById(nonExistentId));
    }

    @Test
    void shouldUpdateUser() {
        appUser.setId(5L);
        appUser.setName(new UserName("Uncle", "Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));

        userOperations.update(5L, appUser);
        ApplicationUser updatedUser = userOperations.getById(5L);

        assertEquals(appUser, updatedUser);
    }

    @Test
    void shouldDeleteUser() {
        assertEquals(3, userOperations.getAll().size());
        userOperations.delete(5L);
        List<ApplicationUser> users = userOperations.getAll();
        assertEquals(2, users.size());
        assertFalse(users.contains(userWithId5));
    }

    @AfterEach
    void cleanupDatabase() {
        jdbcTemplate.execute("delete from users_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from users");
        jdbcTemplate.execute("delete from addresses");
        userWithId5 = null;
        appUser = new ApplicationUser();
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

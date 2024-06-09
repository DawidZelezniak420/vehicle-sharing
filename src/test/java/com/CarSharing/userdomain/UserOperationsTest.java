package com.CarSharing.userdomain;

import com.CarSharing.config.TestBeans;
import com.car.sharing.zelezniak.CarSharingApplication;
import com.car.sharing.zelezniak.userdomain.model.Address;
import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.service.UserOperations;
import com.car.sharing.zelezniak.utils.TimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Value("${create.address.three}")
    private String createAddressThree;
    @Value("${create.address.four}")
    private String createAddressFour;

    @BeforeEach
    void createUsers(){
        jdbcTemplate.execute(createRoleUser);
        jdbcTemplate.execute(createAddressThree);
        jdbcTemplate.execute(createAddressFour);
        jdbcTemplate.execute(createUserFive);
        jdbcTemplate.execute(createUserSix);
        jdbcTemplate.execute(createUserSeven);
        jdbcTemplate.execute(setRoleUserFive);
        jdbcTemplate.execute(setRoleUserSix);
        jdbcTemplate.execute(setRoleUserSeven);
        userWithId5 = createUserWithId5();
    }

    @Test
    void shouldReturnAllUsersWithCorrectData(){
        List<ApplicationUser> allUsers = userOperations.getAll();
        assertTrue(allUsers.contains(userWithId5));
        assertEquals(3,allUsers.size());

        for (ApplicationUser user : allUsers) {
            assertNotNull(user.getId());
            assertNotNull(user.getUsername());
            assertNotNull(user.getCredentials());
            assertNotNull(user.getAddress());
        }
    }

    @Test
    void shouldFindAppUserById(){
        ApplicationUser user = userOperations.findById(5L);
        assertEquals(userWithId5,user);
    }

    @Test
    void shouldAddUser(){
        appUser.setId(1L);
        appUser.setName(new UserName("Uncle","Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com","somepassword"));
        appUser.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Set<ApplicationUser> users = new HashSet<>();
        users.add(appUser);
        Address address = new Address(3L,"teststreet","5","150","Warsaw","00-001","Poland",users);
        appUser.setAddress(address);

        userOperations.add(appUser);

        assertEquals(4,userOperations.getAll().size());
        assertEquals(appUser,userOperations.findById(1L));
    }

    @Test
    void shouldUpdateUser(){
        appUser.setId(5L);
        appUser.setName(new UserName("Uncle","Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com","somepassword"));

        userOperations.update(5L,appUser);
        ApplicationUser updatedUser = userOperations.findById(5L);

        assertEquals(appUser,updatedUser);
    }

    @Test
    void shouldDeleteUser(){

    }

    @AfterEach
    void deleteDataFromDb(){
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
        Set<ApplicationUser> users = new HashSet<>();
        users.add(user);
        Address address = new Address(3L,"teststreet","5","150","Warsaw","00-001","Poland",users);
        user.setAddress(address);
        return user;
    }
}

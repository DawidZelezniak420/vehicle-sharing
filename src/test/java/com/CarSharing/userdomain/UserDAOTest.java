package com.CarSharing.userdomain;

import com.CarSharing.config.TestBeans;
import com.car.sharing.zelezniak.CarSharingApplication;
import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserName;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@Import(TestBeans.class)
class UserDAOTest {

    private static ApplicationUser userWithId5 = createUserWithId5();

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

    @BeforeEach
    void createUsers(){
        jdbcTemplate.execute(createRoleUser);
        jdbcTemplate.execute(createUserFive);
        jdbcTemplate.execute(createUserSix);
        jdbcTemplate.execute(createUserSeven);
        jdbcTemplate.execute(setRoleUserFive);
        jdbcTemplate.execute(setRoleUserSix);
        jdbcTemplate.execute(setRoleUserSeven);
    }

    @Test
    void shouldReturnAllUsersWithCorrectData(){
        List<ApplicationUser> allUsers = userOperations.getAll();
        assertTrue(allUsers.contains(userWithId5));
        assertEquals(3,allUsers.size());

        for (ApplicationUser allUser : allUsers) {
            assertNotNull(allUser.getId());
            assertNotNull(allUser.getUsername());
            assertNotNull(allUser.getCredentials());
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
        appUser.setUserName(new UserName("Uncle","Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com","somepassword"));
        userOperations.add(appUser);
        assertEquals(4,userOperations.getAll().size());
        assertEquals(appUser,userOperations.findById(1L));
    }

    @AfterEach
    void deleteDataFromDb(){
        jdbcTemplate.execute("delete from users_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from users");
    }

    private static ApplicationUser createUserWithId5() {
        ApplicationUser user = new ApplicationUser();
        user.setId(5L);
        user.setUserName(new UserName("UserFive", "Five"));
        user.setCredentials(new UserCredentials("userfive@gmail.com", "somepass"));
        return user;
    }
}

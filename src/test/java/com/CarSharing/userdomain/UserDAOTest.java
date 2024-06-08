package com.CarSharing.userdomain;

import com.CarSharing.config.TestBeans;
import com.car.sharing.zelezniak.CarSharingApplication;
import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
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

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@Import(TestBeans.class)
class UserDAOTest {

    @Autowired
    private ApplicationUser appUser;

    @Autowired
    private UserOperations userDAO;

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
    void shouldReturnAllUsers(){
        Collection<ApplicationUser> allUsers = userDAO.getAll();
        assertEquals(3,allUsers.size());
    }

    @AfterEach
    void deleteDataFromDb(){
        jdbcTemplate.execute("delete from users_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from users");
    }
}

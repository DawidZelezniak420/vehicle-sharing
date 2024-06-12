package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.service.UserOperations;
import com.car.sharing.zelezniak.userdomain.service.authentication.AuthenticationService;
import com.car.sharing.zelezniak.utils.TimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class UserAuthenticationTest {

    private static ApplicationUser userWithId5;

    @Autowired
    private ApplicationUser appUser;

    @Autowired
    private AuthenticationService userAuthentication;

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
    void shouldRegisterNewUser() {
        appUser.setId(null);
        appUser.setName(new UserName("Uncle", "Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));
        appUser.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address( null,"teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        appUser.setAddress(address);

        userAuthentication.register(appUser);

        assertEquals(4, userOperations.getAll().size());
        assertEquals(appUser, userOperations.getById(1L));
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

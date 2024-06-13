package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.userdomain.model.login.LoginRequest;
import com.car.sharing.zelezniak.userdomain.model.login.LoginResponse;
import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import com.car.sharing.zelezniak.userdomain.service.UserOperations;
import com.car.sharing.zelezniak.userdomain.service.authentication.AuthenticationService;
import com.car.sharing.zelezniak.utils.TimeFormatter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationServiceTest {

    private static ApplicationUser userWithId5;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private ApplicationUser appUser;

    @Autowired
    private AuthenticationService userAuthentication;

    @Autowired
    private UserOperations userOperations;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    @Transactional
    @Order(1)
    void shouldRegisterNewUser() {
        appUser.setId(null);
        appUser.setName(new UserName("Uncle", "Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));
        appUser.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address( null,"teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        appUser.setAddress(address);

        userAuthentication.register(appUser);

        assertEquals(4, userRepository.count());
        assertEquals(appUser, userOperations.getById(appUser.getId()));
    }

    @Test
    @Transactional
    @Order(2)
    void shouldRegisterAndLoginUser(){
        appUser.setId(null);
        appUser.setName(new UserName("Uncle", "Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));
        appUser.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address( null,"teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        appUser.setAddress(address);

        userAuthentication.register(appUser);
        LoginRequest loginRequest = new LoginRequest("bob@gmail.com","somepassword");
        LoginResponse login = userAuthentication.login(loginRequest);

        assertEquals(appUser,login.getUser());
        String token = login.getJwt();
        String[] tokenParts = token.split("\\.");
        assertEquals(3,tokenParts.length);
    }


    @Test
    void shouldLoadUserByEmail(){
        assertEquals(userWithId5,userRepository.findByCredentialsEmail("userfive@gmail.com"));
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

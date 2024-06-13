package com.car.sharing.zelezniak;

import com.car.sharing.zelezniak.userdomain.model.login.LoginRequest;
import com.car.sharing.zelezniak.userdomain.model.user.Address;
import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.userdomain.repository.AppUserRepository;
import com.car.sharing.zelezniak.userdomain.service.authentication.AuthenticationService;
import com.car.sharing.zelezniak.utils.TimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = CarSharingApplication.class)
@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTest {

    private static final MediaType APPLICATION_JSON =
            MediaType.APPLICATION_JSON;

    @Autowired
    private ApplicationUser appUser;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void createUser() {
        appUser.setId(null);
        appUser.setName(new UserName("Uncle", "Bob"));
        appUser.setCredentials(new UserCredentials("bob@gmail.com", "somepassword"));
        appUser.setCreatedAt(TimeFormatter.getFormattedActualDateTime());
        Address address = new Address(null, "teststreet", "5", "150", "Warsaw", "00-001", "Poland");
        appUser.setAddress(address);
    }

    @Test
    @Order(1)
    void shouldRegisterUser() throws Exception {
        UserName name = appUser.getName();
        Address address = appUser.getAddress();

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name.firstName").value(name.getFirstName()))
                .andExpect(jsonPath("$.name.lastName").value(name.getLastName()))
                .andExpect(jsonPath("$.credentials.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.address.street").value(address.getStreet()))
                .andExpect(jsonPath("$.address.houseNumber").value(address.getHouseNumber()))
                .andExpect(jsonPath("$.address.flatNumber").value(address.getFlatNumber()))
                .andExpect(jsonPath("$.address.city").value(address.getCity()))
                .andExpect(jsonPath("$.address.postalCode").value(address.getPostalCode()))
                .andExpect(jsonPath("$.address.country").value(address.getCountry()));

        assertEquals(1, userRepository.count());
    }

    @Test
    @Order(2)
    void shouldRegisterAndLoginUser() throws Exception {
        authService.register(appUser);

        LoginRequest loginRequest = new LoginRequest(appUser.getEmail(), "somepassword");

        ApplicationUser registeredUser = userRepository.findByCredentialsEmail(appUser.getEmail());
        UserName name = registeredUser.getName();
        Address address = registeredUser.getAddress();

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.user.name.firstName").value(name.getFirstName()))
                .andExpect(jsonPath("$.user.name.lastName").value(name.getLastName()))
                .andExpect(jsonPath("$.user.credentials.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.user.address.street").value(address.getStreet()))
                .andExpect(jsonPath("$.user.address.houseNumber").value(address.getHouseNumber()))
                .andExpect(jsonPath("$.user.address.flatNumber").value(address.getFlatNumber()))
                .andExpect(jsonPath("$.user.address.city").value(address.getCity()))
                .andExpect(jsonPath("$.user.address.postalCode").value(address.getPostalCode()))
                .andExpect(jsonPath("$.user.address.country").value(address.getCountry()))
                .andExpect(jsonPath("$.jwt").isNotEmpty());
    }

    @AfterEach
    void deleteTestData() {
        jdbcTemplate.execute("delete from users_roles");
        jdbcTemplate.execute("delete from roles");
        jdbcTemplate.execute("delete from users");
        jdbcTemplate.execute("delete from addresses");
    }
}

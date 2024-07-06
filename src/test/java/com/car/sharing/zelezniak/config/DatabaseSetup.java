package com.car.sharing.zelezniak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseSetup {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //user queries
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

    //vehicle queries
    @Value("${create.vehicle.five}")
    private String createVehicleFive;
    @Value("${create.car.five}")
    private String createCarFive;
    @Value("${create.vehicle.six}")
    private String createVehicleSix;
    @Value("${create.motorcycle.six}")
    private String createMotorcycleSix;
    @Value("${create.vehicle.seven}")
    private String createVehicleSeven;
    @Value("${create.car.seven}")
    private String createCarSeven;
    @Value("${create.vehicle.eight}")
    private String createVehicleEight;
    @Value("${create.car.eight}")
    private String createCarEight;
    @Value("${create.vehicle.nine}")
    private String createVehicleNine;
    @Value("${create.motorcycle.nine}")
    private String createMotorcycleNine;


    public void setupClients(){
        executeQueries(createRoleUser, createAddressFive, createAddressSix,
                createAddressSeven, createUserFive, createUserSix,
                createUserSeven, setRoleUserFive, setRoleUserSix, setRoleUserSeven);
    }

    public void setupVehicles(){
        executeQueries(createVehicleFive, createCarFive,
                createVehicleSix, createMotorcycleSix,
                createVehicleSeven, createCarSeven, createVehicleEight,
                createCarEight, createVehicleNine, createMotorcycleNine);
    }

    public void cleanupClients(){
        executeQueries(
                "delete from clients_roles",
                "delete from roles",
                "delete from clients",
                "delete from addresses");
    }

    public void cleanupVehicles(){
        executeQueries(
                "delete from cars",
                "delete from motorcycles",
                "delete from vehicle");
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries)
                .forEach(jdbcTemplate::execute);
    }
}

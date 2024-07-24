package com.vehicle.sharing.zelezniak.config;

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

    //reservation queries
    @Value("${create.reservation.four}")
    private String createReservationFour;
    @Value("${insert.reserved_vehicles.reservation4.vehicle5}")
    private String insertReservedVehiclesreservation4Vehicle5;
    @Value("${create.reservation.five}")
    private String createReservationFive;
    @Value("${insert.reserved_vehicles.reservation5.vehicle5}")
    private String insertReservedVehiclesreservation5Vehicle5;
    @Value("${create.reservation.six}")
    private String createReservationSix;
    @Value("${insert.reserved_vehicles.reservation6.vehicle6}")
    private String insertReservedVehiclesreservation6Vehicle6;
    @Value("${insert.reserved_vehicles.reservation6.vehicle7}")
    private String insertReservedVehiclesreservation6Vehicle7;
    @Value("${create.reservation.seven}")
    private String createReservationSeven;
    @Value("${insert.reserved_vehicles.reservation7.vehicle8}")
    private String insertReservedVehiclesreservation7Vehicle8;
    @Value("${insert.reserved_vehicles.reservation7.vehicle9}")
    private String insertReservedVehiclesreservation7Vehicle9;
    @Value("${create.reservation.eight}")
    private String createReservationEight;
    @Value("${insert.reserved_vehicles.reservation8.vehicle9}")
    private String insertReservedVehiclesreservation8Vehicle9;

    //rent queries
    @Value("${create.rent.five}")
    private String createRentFive;
    @Value("${insert.rented_vehicles.rent5.vehicle5}")
    private String insertRentedVehiclesRent5Vehicle5;
    @Value("${create.rent.six}")
    private String createRentSix;
    @Value("${insert.rented_vehicles.rent6.vehicle6}")
    private String insertRentedVehiclesRent6Vehicle6;
    @Value("${insert.rented_vehicles.rent6.vehicle7}")
    private String insertRentedVehiclesRent6Vehicle7;
    @Value("${create.rent.seven}")
    private String createRentSeven;
    @Value("${insert.rented_vehicles.rent7.vehicle8}")
    private String insertRentedVehiclesRent7Vehicle8;
    @Value("${insert.rented_vehicles.rent7.vehicle9}")
    private String insertRentedVehiclesRent7Vehicle9;

    public void setupAllTables() {
        setupVehicles();
        setupClients();
        setupReservations();
        setupRents();
    }

    public void cleanupAllTables() {
        cleanupReservations();
        cleanupRents();
        cleanupVehicles();
        cleanupClients();
    }

    public void setupClients() {
        executeQueries(createRoleUser, createAddressFive, createAddressSix,
                createAddressSeven, createUserFive, createUserSix,
                createUserSeven, setRoleUserFive, setRoleUserSix, setRoleUserSeven);
    }

    public void setupVehicles() {
        executeQueries(createVehicleFive, createCarFive,
                createVehicleSix, createMotorcycleSix,
                createVehicleSeven, createCarSeven, createVehicleEight,
                createCarEight, createVehicleNine, createMotorcycleNine);
    }

    public void setupReservations() {
        executeQueries(
                createReservationFour, insertReservedVehiclesreservation4Vehicle5,
                createReservationFive, insertReservedVehiclesreservation5Vehicle5,
                createReservationSix, insertReservedVehiclesreservation6Vehicle6,
                insertReservedVehiclesreservation6Vehicle7,
                createReservationSeven,
                insertReservedVehiclesreservation7Vehicle8,
                insertReservedVehiclesreservation7Vehicle9,
                createReservationEight,
                insertReservedVehiclesreservation8Vehicle9);
    }

    public void setupRents() {
        executeQueries(createRentFive, insertRentedVehiclesRent5Vehicle5,
                createRentSix, insertRentedVehiclesRent6Vehicle6,
                insertRentedVehiclesRent6Vehicle7,
                createRentSeven, insertRentedVehiclesRent7Vehicle8,
                insertRentedVehiclesRent7Vehicle9);
    }

    public void cleanupClients() {
        executeQueries(
                "delete from clients_roles",
                "delete from roles",
                "delete from clients",
                "delete from addresses");
    }

    public void cleanupVehicles() {
        executeQueries(
                "delete from cars",
                "delete from motorcycles",
                "delete from vehicles");
    }

    public void cleanupReservations() {
        executeQueries("delete from reserved_vehicles",
                "delete from reservations");
    }

    public void cleanupRents() {
        executeQueries(
                "delete from rented_vehicles",
                "delete from rents");
    }

    private void executeQueries(String... queries) {
        Arrays.stream(queries)
                .forEach(jdbcTemplate::execute);
    }
}

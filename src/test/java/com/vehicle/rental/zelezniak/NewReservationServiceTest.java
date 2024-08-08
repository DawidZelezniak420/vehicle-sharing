package com.vehicle.rental.zelezniak;

import com.vehicle.rental.zelezniak.common_value_objects.RentDuration;
import com.vehicle.rental.zelezniak.common_value_objects.RentInformation;
import com.vehicle.rental.zelezniak.config.*;
import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.reservation_domain.model.util.ReservationCreationRequest;
import com.vehicle.rental.zelezniak.reservation_domain.repository.ReservationRepository;
import com.vehicle.rental.zelezniak.reservation_domain.service.NewReservationService;
import com.vehicle.rental.zelezniak.reservation_domain.service.ReservationService;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VehicleRentalApplication.class)
@TestPropertySource("/application-test.properties")
class NewReservationServiceTest {

    private static Reservation reservationWithId5;
    private Vehicle vehicleWithId6;

    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private ReservationCreator reservationCreator;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private NewReservationService newReservationService;
    @Autowired
    private VehicleCreator vehicleCreator;
    @Autowired
    private RentDurationCreator durationCreator;
    @Autowired
    private LocationCreator locationCreator;

    private ReservationCreationRequest creationRequest;

    @BeforeEach
    void setupDatabase() {
        creationRequest = new ReservationCreationRequest(5L, durationCreator.createDuration2());
        databaseSetup.setupAllTables();
        reservationWithId5 = reservationCreator.createReservationWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupAllTables();
    }

    @Test
    void shouldAddReservationForClient() {
        Long client5Id = 5L;

        Collection<Reservation> allByClientId = reservationRepository.findAllByClientId(client5Id);
        assertEquals(2, allByClientId.size());

        Reservation reservation = newReservationService.addNewReservation(creationRequest);
        Long reservationId = reservation.getId();

        Collection<Reservation> updatedReservations = reservationRepository.findAllByClientId(client5Id);
        assertEquals(3, updatedReservations.size());
        assertEquals(reservation, findReservationById(reservationId));
    }

    @Test
    void shouldUpdateNewReservationLocation() {
        Reservation reservation = newReservationService.addNewReservation(creationRequest);
        reservation.setRentInformation(RentInformation.builder()
                .pickUpLocation(locationCreator.buildTestLocation())
                .build());
        Reservation fromDb = findReservationById(reservation.getId());

        Reservation updated = newReservationService.updateReservation(fromDb, reservation);

        assertEquals(updated, findReservationById(reservation.getId()));
    }

    @Test
    void shouldUpdateLocation() {
        setReservationStatusToNew(reservationWithId5);
        Reservation fromDb = findReservationById(reservationWithId5.getId());
        reservationWithId5.setRentInformation(RentInformation.builder()
                .pickUpLocation(locationCreator.buildTestLocation())
                .build());

        Reservation updated = newReservationService.updateReservation(fromDb, reservationWithId5);

        assertEquals(updated, findReservationById(reservationWithId5.getId()));
    }

    @Test
    void shouldNotUpdateReservation() {
        Reservation newData = reservationWithId5;
        newData.setRentInformation(RentInformation.builder()
                .pickUpLocation(locationCreator.buildTestLocation())
                .build());

        assertThrows(IllegalArgumentException.class,
                () -> newReservationService.updateReservation(reservationWithId5, newData));
    }

    @Test
    void shouldUpdateDuration() {
        Long reservationId = reservationWithId5.getId();
        setReservationStatusToNew(reservationWithId5);

        Collection<Vehicle> vehicles = reservationRepository.findVehiclesByReservationId(reservationId);
        assertEquals(1, vehicles.size());

        Reservation updated = newReservationService.updateDuration(reservationWithId5, durationCreator.createDuration3());

        vehicles = reservationRepository.findVehiclesByReservationId(reservationId);
        assertEquals(0, vehicles.size());
        assertEquals(updated, findReservationById(reservationId));
    }

    @Test
    void shouldNotUpdateDuration() {
        Long reservationId = reservationWithId5.getId();
        Collection<Vehicle> vehicles = reservationRepository.findVehiclesByReservationId(reservationId);

        assertEquals(1, vehicles.size());
        RentDuration duration = durationCreator.createDuration3();

        assertThrows(IllegalArgumentException.class,
                () -> newReservationService.updateDuration(reservationWithId5, duration));
    }

    @Test
    void shouldRemoveReservation() {
        Long client5Id = 5L;

        List<Reservation> initialReservations = (List<Reservation>) reservationRepository.findAllByClientId(client5Id);
        assertEquals(2, initialReservations.size());

        Reservation reservation = initialReservations.get(0);
        setReservationStatusToNew(reservation);
        newReservationService.remove(reservation);

        List<Reservation> updatedReservations = (List<Reservation>) reservationRepository.findAllByClientId(client5Id);
        assertEquals(1, updatedReservations.size());
    }

    @Test
    void shouldNotRemoveReservation() {
        assertThrows(IllegalArgumentException.class,
                () -> newReservationService.remove(reservationWithId5));
    }

    @Test
    void shouldAddVehicleToReservation() {
        Long reservationId = reservationWithId5.getId();
        setReservationStatusToNew(reservationWithId5);

        Collection<Vehicle> vehicles = reservationRepository.findVehiclesByReservationId(reservationId);
        assertEquals(1, vehicles.size());

        vehicleWithId6 = vehicleCreator.createMotorcycleWithId6();
        newReservationService.addVehicleToReservation(reservationWithId5, vehicleWithId6.getId());

        vehicles = reservationRepository.findVehiclesByReservationId(reservationId);
        assertEquals(2, vehicles.size());
    }

    @Test
    void shouldNotAddVehicleToReservation() {
        vehicleWithId6 = vehicleCreator.createMotorcycleWithId6();
        Long vehicle6Id = vehicleWithId6.getId();

        assertThrows(IllegalArgumentException.class,
                () -> newReservationService.addVehicleToReservation(reservationWithId5, vehicle6Id));
    }

    @Test
    void shouldRemoveVehicleFromReservation() {
        Long vehicle5Id = 5L;
        setReservationStatusToNew(reservationWithId5);

        Collection<Vehicle> vehicles = reservationRepository.findVehiclesByReservationId(reservationWithId5.getId());
        assertEquals(1, vehicles.size());

        newReservationService.removeVehicleFromReservation(reservationWithId5, vehicle5Id);

        vehicles = reservationRepository.findVehiclesByReservationId(reservationWithId5.getId());
        assertEquals(0, vehicles.size());
    }

    @Test
    void shouldNotRemoveVehicleFromReservation() {
        Long vehicle5Id = 5L;

        Collection<Vehicle> vehicles = reservationRepository.findVehiclesByReservationId(reservationWithId5.getId());
        assertEquals(1, vehicles.size());

        assertThrows(IllegalArgumentException.class,
                () -> newReservationService.removeVehicleFromReservation(reservationWithId5, vehicle5Id));
    }

    private void setReservationStatusToNew(Reservation reservation) {
        reservation.setReservationStatus(Reservation.ReservationStatus.NEW);
        reservationRepository.save(reservation);
    }

    private Reservation findReservationById(Long reservationId) {
        return reservationService.findById(reservationId);
    }
}

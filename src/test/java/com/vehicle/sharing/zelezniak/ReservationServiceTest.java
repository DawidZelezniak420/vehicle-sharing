package com.vehicle.sharing.zelezniak;

import com.vehicle.sharing.zelezniak.common_value_objects.RentDuration;
import com.vehicle.sharing.zelezniak.config.DatabaseSetup;
import com.vehicle.sharing.zelezniak.config.RentDurationCreator;
import com.vehicle.sharing.zelezniak.config.ReservationCreator;
import com.vehicle.sharing.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.sharing.zelezniak.reservation_domain.repository.ReservationRepository;
import com.vehicle.sharing.zelezniak.reservation_domain.service.ReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
@TestPropertySource("/application-test.properties")
class ReservationServiceTest {

    private static Reservation reservationWithId5;
    private static Pageable pageable = PageRequest.of(0,5);

    @Autowired
    private DatabaseSetup databaseSetup;
    @Autowired
    private ReservationCreator reservationCreator;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RentDurationCreator durationCreator;

    @BeforeEach
    void setupDatabase() {
        databaseSetup.setupAllTables();
        reservationWithId5 = reservationCreator.createReservationWithId5();
    }

    @AfterEach
    void cleanupDatabase() {
        databaseSetup.cleanupAllTables();
    }

    @Test
    void shouldFindAllReservations() {
        Page<Reservation> all = reservationService.findAll(pageable);
        List<Reservation> reservations = all.get().toList();
        assertTrue(reservations.contains(reservationWithId5));
        assertEquals(5, reservations.size());
    }

    @Test
    void shouldFindUnavailableVehicleIdsForReservationInPeriod() {
        RentDuration duration = durationCreator.createDuration1();
        Collection<Long> longs = reservationRepository.unavailableVehicleIdsForReservationInPeriod(
                duration.getRentalStart(), duration.getRentalEnd());

        assertEquals(1, longs.size());
        assertTrue(longs.contains(5L));
    }
}

package com.vehicle.rental.zelezniak;

import com.vehicle.rental.zelezniak.config.RentCreator;
import com.vehicle.rental.zelezniak.config.VehicleCreator;
import com.vehicle.rental.zelezniak.rent_domain.model.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = VehicleRentalApplication.class)
@TestPropertySource("/application-test.properties")
class RentCalculatorTest {

    private static Rent rentWithId5;

    @Autowired
    private RentCreator rentCreator;

    @Autowired
    private ReservationCalculator calculator;

    @Autowired
    private VehicleCreator vehicleCreator;

//    @BeforeEach
//    void setupDatabase() {
//        rentWithId5 = rentCreator.createRentWithId5();
//        rentWithId5.setVehicles(vehicleCreator.createSetWithVehicle5And6());
//    }

//    @Test
//    void shouldCalculateTotalCost() {
//        Money givenValue = new Money(BigDecimal.valueOf(450.00));
//        rentWithId5.setTotalCost(null);
//
//        Money totalCost = calculator.calculateCost(
//                rentWithId5);
//        assertEquals(givenValue,totalCost);
//    }

//    @Test
//    void shouldCalculateTotalCostWith5PercentDiscount() {
//        Money given = new Money(BigDecimal.valueOf(1140.00));
//        LocalDateTime start = LocalDateTime.of(2024, 7, 7, 10, 0, 0);
//        LocalDateTime end = LocalDateTime.of(2024, 7, 15, 10, 0, 0);
//        updateDurationRentWithId5(start,end);
//
//        Money totalCost = calculator.calculateCost(
//                rentWithId5);
//        assertEquals(given,totalCost);
//    }

//    @Test
//    void shouldCalculateTotalCostWith10PercentDiscount() {
//        Money given = new Money(BigDecimal.valueOf(1485.00));
//        LocalDateTime start = LocalDateTime.of(2024, 7, 7, 10, 0, 0);
//        LocalDateTime end = LocalDateTime.of(2024, 7, 18, 10, 0, 0);
//        updateDurationRentWithId5(start,end);
//
//        Money totalCost = calculator.calculateCost(
//                rentWithId5);
//        assertEquals(given,totalCost);
//    }

//    private void updateDurationRentWithId5(
//            LocalDateTime start, LocalDateTime end) {
//        RentInformation rentInformation = rentWithId5.getRentInformation();
//        RentInformation updated = rentInformation.toBuilder()
//                .rentDuration(RentDuration.builder()
//                        .rentalStart(start)
//                        .rentalEnd(end)
//                        .build())
//                .build();
//        rentWithId5.setRentInformation(updated);
//    }
}

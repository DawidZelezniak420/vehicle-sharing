package com.vehicle.rental.zelezniak.reservation_domain.service;

import com.vehicle.rental.zelezniak.common_value_objects.Money;
import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.rental.zelezniak.common_value_objects.RentDuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Component
public class ReservationCalculator {

    private static final BigDecimal FIVE_PERCENT = BigDecimal.valueOf(0.05);
    private static final BigDecimal TEN_PERCENT = BigDecimal.valueOf(0.1);
    private static final int FIVE_DAYS = 5;
    private static final int TEN_DAYS = 10;

    public Money calculateCost(Reservation reservation) {
        long duration = calculateRentDuration(reservation);
        BigDecimal discountPercentage = determineDiscountPercentage(
                duration);
        Money basicRentalCost = calculateBasicCost(
                duration, reservation.getVehicles());
        return applyDiscount(basicRentalCost,
                discountPercentage);
    }

    private long calculateRentDuration(Reservation reservation) {
        var information = reservation.getRentInformation();
        RentDuration rentDuration = information.getRentDuration();
        LocalDateTime rentalStart = rentDuration.getRentalStart();
        LocalDateTime rentalEnd = rentDuration.getRentalEnd();
        return ChronoUnit.DAYS.between(
                rentalStart,rentalEnd);
    }

    private BigDecimal determineDiscountPercentage(
            long duration) {
        if (duration <= FIVE_DAYS) {
            return BigDecimal.ZERO;
        } else if (duration <= TEN_DAYS) {
            return FIVE_PERCENT;
        } else return TEN_PERCENT;
    }

    private Money calculateBasicCost(
            long duration, Set<Vehicle> vehicles) {
        BigDecimal cost = BigDecimal.ZERO;
        for (Vehicle vehicle : vehicles) {
            Money pricePerDay = vehicle.getPricePerDay();
            BigDecimal value = pricePerDay.getValue();
            BigDecimal vehicleRentalCost = value.multiply(
                    BigDecimal.valueOf(duration));
            cost = cost.add(vehicleRentalCost);
        }
        return new Money(cost);
    }


    private Money applyDiscount(
            Money basicCost,
            BigDecimal discountPercentage) {
        BigDecimal basic = basicCost.getValue();
        BigDecimal discountValue = basic.multiply(
                discountPercentage);
        BigDecimal afterDiscount = basic.subtract(
                discountValue);
        return new Money(afterDiscount);
    }
}

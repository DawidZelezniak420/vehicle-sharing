package com.vehicle.sharing.zelezniak.rent_domain.service;

import com.vehicle.sharing.zelezniak.common_value_objects.Money;
import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Component
public class RentCalculator {

    private static final BigDecimal FIVE_PERCENT = BigDecimal.valueOf(0.05);
    private static final BigDecimal TEN_PERCENT = BigDecimal.valueOf(0.1);
    private static final int FIVE_DAYS = 5;
    private static final int TEN_DAYS = 10;

    public Money calculateTotalCost(Rent rent) {
        long duration = calculateRentDuration(rent);
        BigDecimal discountPercentage = determineDiscountPercentage(
                duration);
        Money basicRentalCost = calculateBasicCost(
                duration, rent.getVehicles());
        return applyDiscount(basicRentalCost,
                discountPercentage);
    }

    private long calculateRentDuration(Rent rent) {
        var information = rent.getRentInformation();
        LocalDateTime rentalStart = information.getRentalStart();
        LocalDateTime rentalEnd = information.getRentalEnd();
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

package com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class Year {

    private final int year;

    public Year(int year) {
        validate(year);
        this.year = year;
    }

    private void validate(int year) {
        if(isInFuture(year) || year < 1900){
            throwException();
        }
    }

    private boolean isInFuture(int year) {
        return year > LocalDate.now().getYear();
    }

    private void throwException() {
    throw new IllegalArgumentException("Year incorrect.");
    }
}

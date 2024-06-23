package com.car.sharing.zelezniak.sharingdomain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
public class Year {

    private final int year;

    public Year(int year) {
        validate(year);
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Year year1 = (Year) o;
        return year == year1.year;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(year);
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

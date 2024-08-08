package com.vehicle.rental.zelezniak.common_value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class RentDuration {

    private static final String MUST_BE_IN_FUTURE = " must be in future";

    @FutureOrPresent(message = "Rental start" + MUST_BE_IN_FUTURE)
    private final LocalDateTime rentalStart;

    @Future(message = "Rental end" + MUST_BE_IN_FUTURE)
    private final LocalDateTime rentalEnd;

    public RentDuration(LocalDateTime start, LocalDateTime end) {
        validateArguments(start, end);
        this.rentalStart = start;
        this.rentalEnd = end;
    }

    private void validateArguments(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(
                    "Rental start can not be after rental end.");
        }
    }
}

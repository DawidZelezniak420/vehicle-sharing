package com.vehicle.sharing.zelezniak.rent_domain.model.rent_value_objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class RentInformation {

    private static final String MUST_BE_IN_FUTURE = " must be in future";

    @FutureOrPresent(message = "Rental start" + MUST_BE_IN_FUTURE)
    private final LocalDateTime rentalStart;

    @Future(message = "Rental end" + MUST_BE_IN_FUTURE)
    private final LocalDateTime rentalEnd;

    @Embedded
    private final Location pickUpLocation;
}

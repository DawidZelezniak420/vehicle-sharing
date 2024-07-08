package com.vehicle.sharing.zelezniak.rent_domain.model.value_objects;

import com.vehicle.sharing.zelezniak.value_objects.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class RentInformation {

    private static final String MUST_BE_IN_FUTURE = " must be in future";

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "total_cost"))
    private final Money totalCost;

    @FutureOrPresent(message = "Rental start" + MUST_BE_IN_FUTURE)
    private final LocalDateTime rentalStart;

    @Future(message = "Rental end" + MUST_BE_IN_FUTURE)
    private final LocalDateTime rentalEnd;

    @Embedded
    private final Location pickUpLocation;

}

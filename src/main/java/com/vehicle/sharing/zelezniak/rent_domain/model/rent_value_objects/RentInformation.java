package com.vehicle.sharing.zelezniak.rent_domain.model.rent_value_objects;

import com.vehicle.sharing.zelezniak.common_value_objects.Money;
import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.model.util.RentUpdateVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@EqualsAndHashCode
@ToString
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

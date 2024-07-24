package com.vehicle.sharing.zelezniak.common_value_objects;

import com.vehicle.sharing.zelezniak.rent_domain.model.rent_value_objects.Location;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class RentInformation {

    @Embedded
    private final RentDuration rentDuration;

    @Embedded
    private final Location pickUpLocation;
}

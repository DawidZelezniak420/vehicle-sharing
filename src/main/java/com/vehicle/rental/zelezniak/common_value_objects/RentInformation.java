package com.vehicle.rental.zelezniak.common_value_objects;

import jakarta.persistence.*;
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
    @AttributeOverrides({
            @AttributeOverride(name = "city.cityName", column = @Column(name = "pick_up_city")),
            @AttributeOverride(name = "street.streetName", column = @Column(name = "pick_up_street")),
            @AttributeOverride(name = "additionalInformation", column = @Column(name = "pick_up_additional_info"))
    })
    private final Location pickUpLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city.cityName", column = @Column(name = "drop_off_city")),
            @AttributeOverride(name = "street.streetName", column = @Column(name = "drop_off_street")),
            @AttributeOverride(name = "additionalInformation", column = @Column(name = "drop_off_additional_info"))
    })
    private final Location dropOffLocation;
}

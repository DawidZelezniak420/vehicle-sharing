package com.vehicle.rental.zelezniak.common_value_objects;

import com.vehicle.rental.zelezniak.common_value_objects.address.City;
import com.vehicle.rental.zelezniak.common_value_objects.address.Street;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
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
public class Location {

    @Embedded
    @AttributeOverride(
            name = "cityName",
            column = @Column(name = "city"))
    private final City city;

    @Embedded
    @AttributeOverride(
            name = "streetName",
            column = @Column(name = "street"))
    private final Street street;

    private final String additionalInformation;
}

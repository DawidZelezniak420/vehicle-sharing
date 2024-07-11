package com.vehicle.sharing.zelezniak.rent_domain.model.rent_value_objects;

import com.vehicle.sharing.zelezniak.common_value_objects.*;
import jakarta.persistence.*;
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
            name = "countryName",
            column = @Column(name = "country"))
    private final Country country;

    @Embedded
    @AttributeOverride(
            name = "districtName",
            column = @Column(name = "district"))
    private final District district;

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

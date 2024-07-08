package com.vehicle.sharing.zelezniak.rent_domain.model.value_objects;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Location {

    private final String country;
    private final String district;
    private final String city;
    private final String street;
    private final String additionalInformation;
}

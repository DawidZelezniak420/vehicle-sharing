package com.vehicle.sharing.zelezniak.value_objects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class District {

    private final String districtName;
}

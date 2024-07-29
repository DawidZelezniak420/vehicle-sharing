package com.vehicle.rental.zelezniak.common_value_objects.address;

import com.vehicle.rental.zelezniak.constants.ValidationMessages;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class Country {

    @NotBlank(message = "Country name" + ValidationMessages.CAN_NOT_BE_BLANK)
    private final String countryName;
}

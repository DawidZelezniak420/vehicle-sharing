package com.vehicle.sharing.zelezniak.value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_BLANK;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class Street {

    @NotBlank(message = "Street name" + CAN_NOT_BE_BLANK)
    private final String streetName;
}

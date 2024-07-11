package com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_BLANK;

@Embeddable
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationNumber {

    @NotBlank(message = "Registration number" + CAN_NOT_BE_BLANK)
    private final String registration;
}

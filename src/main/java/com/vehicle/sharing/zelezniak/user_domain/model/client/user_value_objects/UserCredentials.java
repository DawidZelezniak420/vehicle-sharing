package com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;

@Getter
@Embeddable
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class UserCredentials {

    private static final String INVALID_PASSWORD = "Password must contains at least 5 characters.";

    @NotNull(message = "Email address" + CAN_NOT_BE_NULL)
    private final String email;

    @NotNull(message = INVALID_PASSWORD)
    @Size(min = 5, message = INVALID_PASSWORD)
    private final String password;
}

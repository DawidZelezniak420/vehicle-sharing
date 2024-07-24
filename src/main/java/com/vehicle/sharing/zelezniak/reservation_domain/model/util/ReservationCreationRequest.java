package com.vehicle.sharing.zelezniak.reservation_domain.model.util;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationCreationRequest {

    @NotNull(message = "Rent" + CAN_NOT_BE_NULL)
    private Rent rent;

    @Min(value = 1, message = "Client id can not be lower than 1")
    private Long clientId;

    @NotNull(message = "Vehicles IDs" + CAN_NOT_BE_NULL)
    private Set<Long> vehiclesIds;
}

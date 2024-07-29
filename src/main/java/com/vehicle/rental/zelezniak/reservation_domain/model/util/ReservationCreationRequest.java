package com.vehicle.rental.zelezniak.reservation_domain.model.util;

import com.vehicle.rental.zelezniak.constants.ValidationMessages;
import com.vehicle.rental.zelezniak.reservation_domain.model.Reservation;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationCreationRequest {

    @NotNull(message = "Reservation" + ValidationMessages.CAN_NOT_BE_NULL)
    private Reservation reservation;

    @Min(value = 1, message = "Client id can not be lower than 1")
    private Long clientId;

    @NotNull(message = "Vehicles IDs" + ValidationMessages.CAN_NOT_BE_NULL)
    private Set<Long> vehiclesIds;
}

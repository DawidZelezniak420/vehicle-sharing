package com.car.sharing.zelezniak.sharingdomain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Embeddable
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class VehicleInformation {

    private static final String CAN_NOT_BE_BLANK = " can not be blank.";

    @NotBlank(message = "Brand" + CAN_NOT_BE_BLANK)
    private final String brand;

    @NotBlank(message = "Model" + CAN_NOT_BE_BLANK)
    private final String model;

    @NotBlank(message = "Registration number" + CAN_NOT_BE_BLANK)
    private final String registrationNumber;

    @NotNull(message = "Production year must be specified.")
    private final Year productionYear;

    @NotBlank(message = "Description" + CAN_NOT_BE_BLANK)
    private final String description;

}
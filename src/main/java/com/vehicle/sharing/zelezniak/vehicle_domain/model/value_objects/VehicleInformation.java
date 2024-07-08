package com.vehicle.sharing.zelezniak.vehicle_domain.model.value_objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.*;

@Getter
@Embeddable
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class VehicleInformation {

    @NotBlank(message = "Brand" + CAN_NOT_BE_BLANK)
    private final String brand;

    @NotBlank(message = "Model" + CAN_NOT_BE_BLANK)
    private final String model;

    @NotBlank(message = "Registration number" + CAN_NOT_BE_BLANK)
    private final String registrationNumber;

    @Embedded
    @AttributeOverride(
            name = "year",
            column = @Column(name = "production_year"))
    private final Year productionYear;

    @NotBlank(message = "Description" + CAN_NOT_BE_BLANK)
    private final String description;

    @Embedded
    private final Engine engine;

    @Enumerated(EnumType.STRING)
    private final GearType gearType;

    public enum GearType {
        MANUAL,
        AUTOMATIC,
        CVT,
        DUAL_CLUTCH
    }
}
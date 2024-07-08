package com.vehicle.sharing.zelezniak.vehicle_domain.model.value_objects;

import com.vehicle.sharing.zelezniak.constants.ValidationMessages;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@AttributeOverrides(
        @AttributeOverride(name = "productionYear.year",
                column = @Column(name = "production_year"))
)
public class VehicleInformation {

    @NotBlank(message = "Brand" + ValidationMessages.CAN_NOT_BE_BLANK)
    private final String brand;

    @NotBlank(message = "Model" + ValidationMessages.CAN_NOT_BE_BLANK)
    private final String model;

    @NotBlank(message = "Registration number" + ValidationMessages.CAN_NOT_BE_BLANK)
    private final String registrationNumber;

    @NotNull(message = "Production year" + ValidationMessages.MUST_BE_SPECIFIED)
    @Embedded
    private final Year productionYear;

    @NotBlank(message = "Description" + ValidationMessages.CAN_NOT_BE_BLANK)
    private final String description;

    @NotNull(message = "Engine" + ValidationMessages.MUST_BE_SPECIFIED)
    @Embedded
    private final Engine engine;

    @Enumerated(EnumType.STRING)
    private final GearType gearType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleInformation that = (VehicleInformation) o;
        return Objects.equals(brand, that.brand)
                && Objects.equals(model, that.model)
                && Objects.equals(registrationNumber, that.registrationNumber)
                && Objects.equals(productionYear, that.productionYear)
                && Objects.equals(description, that.description)
                && Objects.equals(engine, that.engine)
                && gearType == that.gearType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                brand, model,
                registrationNumber,
                productionYear,
                description,
                engine, gearType);
    }

    @Override
    public String toString() {
        return "VehicleInformation{" +
                "brand='" + brand +
                ", model='" + model +
                ", registrationNumber='" + registrationNumber +
                ", productionYear=" + productionYear +
                ", description='" + description +
                ", engine=" + engine +
                '}';
    }

    public enum GearType {
        MANUAL,
        AUTOMATIC,
        CVT,
        DUAL_CLUTCH
    }
}
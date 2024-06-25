package com.car.sharing.zelezniak.sharing_domain.model.value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static com.car.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_BLANK;
import static com.car.sharing.zelezniak.constants.ValidationMessages.MUST_BE_SPECIFIED;

@Getter
@Embeddable
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class VehicleInformation {

    @NotBlank(message = "Brand" + CAN_NOT_BE_BLANK)
    private final String brand;

    @NotBlank(message = "Model" + CAN_NOT_BE_BLANK)
    private final String model;

    @NotBlank(message = "Registration number" + CAN_NOT_BE_BLANK)
    private final String registrationNumber;

    @NotNull(message = "Production year" + MUST_BE_SPECIFIED)
    private final Year productionYear;

    @NotBlank(message = "Description" + CAN_NOT_BE_BLANK)
    private final String description;

    @NotNull(message = "Engine" + MUST_BE_SPECIFIED)
    private final Engine engine;

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
                && Objects.equals(engine, that.engine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                brand, model,
                registrationNumber,
                productionYear,
                description, engine);
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
}
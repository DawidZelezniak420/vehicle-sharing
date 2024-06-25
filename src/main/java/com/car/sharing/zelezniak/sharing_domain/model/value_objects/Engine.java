package com.car.sharing.zelezniak.sharing_domain.model.value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static com.car.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_BLANK;

@Embeddable
@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Engine {

    private static final String CANT_BE_BELOW_ONE = " can not be lower than 1";

    @NotBlank(message = "Engine type" + CAN_NOT_BE_BLANK)
    private final String engineType;

    @NotBlank(message = "Fuel type" + CAN_NOT_BE_BLANK)
    private final String fuelType;

    @Min(value = 1,message = "Horse power" + CANT_BE_BELOW_ONE)
    private final int horsepower;

    @Min(value = 1,message = "Cylinders number" + CANT_BE_BELOW_ONE)
    private final int cylinders;

    @Min(value = 1,message = "Displacement" + CANT_BE_BELOW_ONE)
    private final double displacement;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engine engine = (Engine) o;
        return horsepower == engine.horsepower
                && cylinders == engine.cylinders
                && Double.compare(displacement, engine.displacement) == 0
                && Objects.equals(engineType, engine.engineType)
                && Objects.equals(fuelType, engine.fuelType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engineType, fuelType, horsepower, cylinders, displacement);
    }

    @Override
    public String toString() {
        return "Engine{" +
                "engineType='" + engineType +
                ", fuelType='" + fuelType +
                ", horsepower=" + horsepower +
                ", cylinders=" + cylinders +
                ", displacement=" + displacement +
                '}';
    }
}

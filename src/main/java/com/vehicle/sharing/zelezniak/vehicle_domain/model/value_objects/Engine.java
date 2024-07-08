package com.vehicle.sharing.zelezniak.vehicle_domain.model.value_objects;

import com.vehicle.sharing.zelezniak.constants.ValidationMessages;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Engine {

    private static final String CANT_BE_BELOW_ONE = " can not be lower than 1";

    @NotBlank(message = "Engine type" + ValidationMessages.CAN_NOT_BE_BLANK)
    private final String engineType;

    @NotBlank(message = "Fuel type" + ValidationMessages.CAN_NOT_BE_BLANK)
    @Enumerated(EnumType.STRING)
    private final FuelType fuelType;

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
        return Objects.hash(
                engineType,
                fuelType,
                horsepower,
                cylinders,
                displacement);
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

    public enum FuelType {
        GASOLINE,
        GASOLINE_WITH_GAS,
        DIESEL,
        ELECTRIC,
        HYBRID,
    }
}

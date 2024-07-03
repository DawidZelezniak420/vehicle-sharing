package com.car.sharing.zelezniak.sharing_domain.model.vehicles;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static com.car.sharing.zelezniak.constants.ValidationMessages.MUST_BE_SPECIFIED;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "cars")
public class Car extends Vehicle {

    @NotNull(message = "Number of doors" + MUST_BE_SPECIFIED)
    private Integer doorsNumber;

    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    public Car update(VehicleUpdateVisitor visitor,
                      Vehicle newData) {
        if (newData instanceof Car updated)
            return visitor.update(this, updated);
        throw new IllegalArgumentException("Provided data is not an instance of Car");
    }

    @Override
    public String toString() {
        return "Car{" +
                "doorsNumber=" + doorsNumber +
                ", driveType=" + driveType +
                ", bodyType=" + bodyType +
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(doorsNumber, car.doorsNumber)
                && driveType == car.driveType
                && bodyType == car.bodyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                doorsNumber,
                driveType,
                bodyType);
    }

    public enum DriveType {
        FOUR_WHEEL_DRIVE,
        FRONT_WHEEL_DRIVE,
        REAR_WHEEL_DRIVE,
    }

    public enum BodyType {
        SUV,
        SEDAN,
        COUPE,
        HATCHBACK
    }
}

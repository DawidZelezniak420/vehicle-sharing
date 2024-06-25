package com.car.sharing.zelezniak.sharing_domain.model.vehicles;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.car.sharing.zelezniak.constants.ValidationMessages.MUST_BE_SPECIFIED;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("car")
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Car extends Vehicle {

    @NotNull(message = "Number of doors" + MUST_BE_SPECIFIED)
    private Integer doorsNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Car type" + MUST_BE_SPECIFIED)
    private CarType carType;

    public void update(VehicleUpdateVisitor visitor, Vehicle newData) {
        if(newData instanceof Car updated){
            visitor.update(this,updated);
        }
    }

    public enum CarType {
        SUV,
        SEDAN,
        COUPE,
        HATCHBACK
    }
}

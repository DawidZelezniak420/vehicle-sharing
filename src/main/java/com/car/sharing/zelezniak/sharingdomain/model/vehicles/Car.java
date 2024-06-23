package com.car.sharing.zelezniak.sharingdomain.model.vehicles;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("car")
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Car extends Vehicle {

    private Integer numberOfDoors;
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

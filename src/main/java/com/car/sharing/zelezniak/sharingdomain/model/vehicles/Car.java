package com.car.sharing.zelezniak.sharingdomain.model.vehicles;

import com.car.sharing.zelezniak.sharingdomain.model.valueobjects.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("car")
public class Car extends Vehicle {

    private Integer numberOfDoors;
    private CarType carType;

    public Car(String brand, String model,
               String registrationNumber, String productionYear,
               Money pricePerDay, Status status, String description,
               Integer numberOfDoors, CarType carType) {
        super(brand, model, registrationNumber,
                productionYear, pricePerDay,
                status, description);
        this.numberOfDoors = numberOfDoors;
        this.carType = carType;
    }

    public enum CarType {
        SUV,
        SEDAN,
        COUPE,
        HATCHBACK
    }
}

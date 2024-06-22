package com.car.sharing.zelezniak.sharingdomain.model.vehicles;

import com.car.sharing.zelezniak.sharingdomain.model.valueobjects.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("motorcycle")
public class Motorcycle extends Vehicle {

    private MotorcycleType motorcycleType;

    public Motorcycle(String brand, String model,
                      String registrationNumber, String productionYear,
                      Money pricePerDay, Status status, String description,
                      MotorcycleType motorcycleType) {
        super(brand, model, registrationNumber,
                productionYear, pricePerDay,
                status, description);
    this.motorcycleType = motorcycleType;
    }

    public enum MotorcycleType{
        SPORT,
        CRUISER,
        CHOPPER,
        BOBBER
    }
}

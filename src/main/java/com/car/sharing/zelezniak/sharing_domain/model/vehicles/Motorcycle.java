package com.car.sharing.zelezniak.sharing_domain.model.vehicles;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
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
@DiscriminatorValue("motorcycle")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Motorcycle extends Vehicle {

    private MotorcycleType motorcycleType;

    public void update(VehicleUpdateVisitor visitor, Vehicle newData) {
        if(newData instanceof Motorcycle updated){
            visitor.update(this,updated);
        }
    }

    public enum MotorcycleType{
        SPORT,
        CRUISER,
        CHOPPER,
        BOBBER
    }
}

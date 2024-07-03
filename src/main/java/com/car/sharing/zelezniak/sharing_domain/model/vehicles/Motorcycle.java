package com.car.sharing.zelezniak.sharing_domain.model.vehicles;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "motorcycles")
public class Motorcycle extends Vehicle {

    @Enumerated(EnumType.STRING)
    private MotorcycleType motorcycleType;

    public Motorcycle update(VehicleUpdateVisitor visitor,
                             Vehicle newData) {
        if (newData instanceof Motorcycle updated) {
            visitor.update(this, updated);
        }
        throw new IllegalArgumentException("Provided data is not an instance of Motorcycle");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Motorcycle that = (Motorcycle) o;
        return motorcycleType == that.motorcycleType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(motorcycleType);
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "motorcycleType=" + motorcycleType +
                '}' + super.toString();
    }

    public enum MotorcycleType {
        SPORT,
        CRUISER,
        CHOPPER,
        BOBBER
    }
}

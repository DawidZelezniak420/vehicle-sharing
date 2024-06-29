package com.car.sharing.zelezniak.sharing_domain.model.vehicles;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Money;
import com.car.sharing.zelezniak.sharing_domain.model.value_objects.VehicleInformation;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.util.VehicleUpdateVisitor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static com.car.sharing.zelezniak.constants.ValidationMessages.MUST_BE_SPECIFIED;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Car.class, name = "car"),
        @JsonSubTypes.Type(value = Motorcycle.class, name = "motorcycle")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@AllArgsConstructor
@Table(name = "vehicle")
@SuperBuilder(toBuilder = true)
@AttributeOverrides(
        @AttributeOverride(name = "pricePerDay.money",
                column = @Column(name = "price_per_day"))
)
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VehicleInformation vehicleInformation;

    @NotNull(message = "Price per day" + MUST_BE_SPECIFIED)
    private Money pricePerDay;

    @Enumerated(EnumType.STRING)
    private Status status;

    protected Vehicle() {
    }

    public abstract void update(VehicleUpdateVisitor visitor,
                                Vehicle newData);

    public String getRegistrationNumber(){
        return vehicleInformation.getRegistrationNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vehicleInformation, vehicle.vehicleInformation)
                && Objects.equals(pricePerDay, vehicle.pricePerDay)
                && status == vehicle.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                vehicleInformation,
                pricePerDay, status);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", vehicleInformation=" + vehicleInformation +
                ", pricePerDay=" + pricePerDay +
                ", status=" + status +
                '}';
    }

    public enum Status {
        AVAILABLE,
        UNAVAILABLE
    }

}

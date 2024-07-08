package com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles;

import com.vehicle.sharing.zelezniak.value_objects.Money;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.value_objects.VehicleInformation;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util.VehicleUpdateVisitor;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.MUST_BE_SPECIFIED;

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
@Table(name = "vehicles")
@SuperBuilder(toBuilder = true)
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VehicleInformation vehicleInformation;

    @NotNull(message = "Price per day" + MUST_BE_SPECIFIED)
    @AttributeOverride(
            name = "value",
            column = @Column(name = "price_per_day"))
    private Money pricePerDay;

    @Enumerated(EnumType.STRING)
    private Status status;

    protected Vehicle() {
    }

    public String getRegistrationNumber() {
        return vehicleInformation.getRegistrationNumber();
    }

    public abstract Vehicle update(VehicleUpdateVisitor visitor,
                                   Vehicle newData);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vehicle vehicle = (Vehicle) object;
        return Objects.equals(id, vehicle.id)
                && Objects.equals(vehicleInformation, vehicle.vehicleInformation)
                && Objects.equals(pricePerDay, vehicle.pricePerDay)
                && status == vehicle.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
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

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        AVAILABLE("available"),
        UNAVAILABLE("unavailable");

        private final String value;

        public static Status getStatusFromString(String s) {
            for (Status status : Status.values()) {
                if (s.equals(status.getValue())) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status value: " + s);
        }
    }

}

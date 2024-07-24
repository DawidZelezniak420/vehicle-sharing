package com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vehicle.sharing.zelezniak.common_value_objects.Money;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.VehicleInformation;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util.VehicleUpdateVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;

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

    @NotNull(message = "Price per day" + CAN_NOT_BE_NULL)
    @AttributeOverride(
            name = "value",
            column = @Column(name = "price_per_day"))
    private Money pricePerDay;

    @NotNull(message = "Deposit" + CAN_NOT_BE_NULL)
    @AttributeOverride(
            name = "value",
            column = @Column(name = "deposit"))
    private Money deposit;

    @Enumerated(EnumType.STRING)
    private Status status;

    protected Vehicle() {
    }

    public RegistrationNumber getRegistrationNumber() {
        return vehicleInformation.getRegistrationNumber();
    }

    public abstract  Vehicle update(
            VehicleUpdateVisitor visitor, Vehicle newData);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vehicle vehicle = (Vehicle) object;
        return Objects.equals(id, vehicle.id)
                && Objects.equals(vehicleInformation, vehicle.vehicleInformation)
                && Objects.equals(pricePerDay, vehicle.pricePerDay)
                && Objects.equals(deposit, vehicle.deposit)
                && status == vehicle.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, vehicleInformation,
                pricePerDay, deposit,
                status);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", vehicleInformation=" + vehicleInformation +
                ", pricePerDay=" + pricePerDay +
                ", deposit=" + deposit +
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
                if (s.equalsIgnoreCase(status.getValue())) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status value: " + s);
        }
    }

}

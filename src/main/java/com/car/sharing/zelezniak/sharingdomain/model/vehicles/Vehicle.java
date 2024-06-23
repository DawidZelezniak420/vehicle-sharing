package com.car.sharing.zelezniak.sharingdomain.model.vehicles;

import com.car.sharing.zelezniak.sharingdomain.model.valueobjects.Money;
import com.car.sharing.zelezniak.sharingdomain.model.valueobjects.VehicleInformation;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Car.class, name = "car"),
        @JsonSubTypes.Type(value = Motorcycle.class, name = "motorcycle")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
@SuperBuilder
public abstract class Vehicle {

    private static final String MUST_BE_SPECIFIED = " must be specified.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VehicleInformation vehicleInformation;

    @NotNull(message = "Price per day" + MUST_BE_SPECIFIED)
    private Money pricePerDay;

    @NotNull(message = "Status" + MUST_BE_SPECIFIED)
    @Enumerated(EnumType.STRING)
    private Status status;

    public abstract void update(VehicleUpdateVisitor visitor,
                                Vehicle newData);

    public String getRegistrationNumber(){
        return vehicleInformation.getRegistrationNumber();
    }

    public String getDescription(){
        return vehicleInformation.getDescription();
    }

    public enum Status {
        AVAILABLE,
        UNAVAILABLE
    }
}

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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@AllArgsConstructor
@Table(name = "vehicles")
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

    public String getDescription(){
        return vehicleInformation.getDescription();
    }


    public enum Status {
        AVAILABLE,
        UNAVAILABLE
    }
}

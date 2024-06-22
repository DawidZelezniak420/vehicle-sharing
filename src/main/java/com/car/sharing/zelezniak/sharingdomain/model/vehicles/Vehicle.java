package com.car.sharing.zelezniak.sharingdomain.model.vehicles;

import com.car.sharing.zelezniak.sharingdomain.model.valueobjects.Money;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
@Table(name = "vehicles")
public abstract class Vehicle {

    private static final String CAN_NOT_BE_BLANK = " can not be blank.";
    private static final String MUST_BE_SPECIFIED = " must be specified.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Brand" + CAN_NOT_BE_BLANK)
    private final String brand;

    @NotBlank(message = "Model" + CAN_NOT_BE_BLANK)
    private final String model;

    @NotBlank(message = "Registration number" + CAN_NOT_BE_BLANK)
    private String registrationNumber;

    @NotBlank(message = "Production year" + CAN_NOT_BE_BLANK)
    private final String productionYear;

    @NotNull(message = "Price per day" + MUST_BE_SPECIFIED)
    private Money pricePerDay;

    @NotNull(message = "Status" + MUST_BE_SPECIFIED)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotBlank(message = "Description" + CAN_NOT_BE_BLANK)
    private String description;

    public Vehicle(String brand, String model,
                   String registrationNumber, String productionYear,
                   Money pricePerDay, Status status, String description) {
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.productionYear = productionYear;
        this.pricePerDay = pricePerDay;
        this.status = status;
        this.description = description;
    }

    public Vehicle() {
        brand = null;
        model = null;
        productionYear = null;
    }

    public enum Status {
        AVAILABLE,
        UNAVAILABLE
    }
}

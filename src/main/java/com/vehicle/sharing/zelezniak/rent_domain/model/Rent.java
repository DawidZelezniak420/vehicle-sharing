package com.vehicle.sharing.zelezniak.rent_domain.model;

import com.vehicle.sharing.zelezniak.common_value_objects.Money;
import com.vehicle.sharing.zelezniak.rent_domain.model.rent_value_objects.RentInformation;
import com.vehicle.sharing.zelezniak.rent_domain.model.util.RentUpdateVisitor;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import static java.util.Objects.isNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rents")
@Builder(toBuilder = true)
@ToString
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private RentInformation rentInformation;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "total_cost"))
    private Money totalCost;

    @Enumerated(EnumType.STRING)
    private RentStatus rentStatus;

    @ManyToOne(
            cascade = {
                    CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany(
            cascade = {
                    CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "rented_vehicles",
            joinColumns = @JoinColumn(name = "rent_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles;

    public void addVehicles(Collection<Vehicle> vehicles) {
        initializeVehiclesIfNull();
        this.vehicles.addAll(vehicles);
    }

    public Rent updateRent(
            RentUpdateVisitor updateVisitor,
            Rent newData) {
        return updateVisitor.updateRent(
                this, newData);
    }

    public enum RentStatus {
        ACTIVE,
        COMPLETED
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Rent rent = (Rent) object;
        return Objects.equals(id, rent.id)
                && Objects.equals(rentInformation, rent.rentInformation)
                && Objects.equals(totalCost, rent.totalCost)
                && rentStatus == rent.rentStatus
                && Objects.equals(client, rent.client);
    }

    public int hashCode() {
        return Objects.hash(
                id, rentInformation,
                totalCost, rentStatus,
                client);
    }

    private void initializeVehiclesIfNull() {
        if (isNull(vehicles)) {
            vehicles = new HashSet<>();
        }
    }
}

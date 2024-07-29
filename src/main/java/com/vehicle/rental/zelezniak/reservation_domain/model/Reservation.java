package com.vehicle.rental.zelezniak.reservation_domain.model;

import com.vehicle.rental.zelezniak.common_value_objects.Money;
import com.vehicle.rental.zelezniak.common_value_objects.RentInformation;
import com.vehicle.rental.zelezniak.reservation_domain.model.util.ReservationUpdateVisitor;
import com.vehicle.rental.zelezniak.user_domain.model.client.Client;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.isNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
@Builder(toBuilder = true)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private RentInformation rentInformation;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "estimated_cost"))
    private Money estimatedCost;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "total_deposit"))
    private Money totalDeposit;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

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
            name = "reserved_vehicles",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles;

    public enum ReservationStatus {
        NEW,
        ACTIVE,
        CANCELLED,
        COMPLETED
    }

    public Reservation updateReservation(
            ReservationUpdateVisitor updateVisitor,
            Reservation newData) {
        return updateVisitor.updateReservation(
                this, newData);
    }

    public void addVehicleToReservation(
            Vehicle vehicle){
      initializeVehiclesIfNull();
      vehicles.add(vehicle);
    }

    public void addVehiclesToReservation(
            Collection<Vehicle> v){
        initializeVehiclesIfNull();
        vehicles.addAll(v);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Reservation that = (Reservation) object;
        return Objects.equals(id, that.id)
                && Objects.equals(rentInformation, that.rentInformation)
                && Objects.equals(estimatedCost, that.estimatedCost)
                && reservationStatus == that.reservationStatus
                && Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                rentInformation, estimatedCost,
                reservationStatus, client);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", rentInformation=" + rentInformation +
                ", estimatedCost=" + estimatedCost +
                ", reservationStatus=" + reservationStatus +
                ", client=" + client +
                '}';
    }

    private void initializeVehiclesIfNull() {
        if(isNull(vehicles)){
            vehicles = new HashSet<>();
        }
    }
}

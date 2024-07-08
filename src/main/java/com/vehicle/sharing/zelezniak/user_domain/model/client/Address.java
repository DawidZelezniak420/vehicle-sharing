package com.vehicle.sharing.zelezniak.user_domain.model.client;

import com.vehicle.sharing.zelezniak.value_objects.City;
import com.vehicle.sharing.zelezniak.value_objects.Country;
import com.vehicle.sharing.zelezniak.value_objects.Street;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
@Builder
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Embedded
    @AttributeOverride(
            name = "streetName",
            column = @Column(name = "street"))
    private Street street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "flat_number")
    private String flatNumber;

    @Embedded
    @AttributeOverride(
            name = "cityName",
            column = @Column(name = "city"))
    private City city;

    @Column(name = "postal_code")
    private String postalCode;

    @Embedded
    @AttributeOverride(
            name = "countryName",
            column = @Column(name = "country"))
    private Country country;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Address address = (Address) object;
        return Objects.equals(id, address.id)
                && Objects.equals(street, address.street)
                && Objects.equals(houseNumber, address.houseNumber)
                && Objects.equals(flatNumber, address.flatNumber)
                && Objects.equals(city, address.city)
                && Objects.equals(postalCode, address.postalCode)
                && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street,
                houseNumber, flatNumber,
                city, postalCode, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", flatNumber='" + flatNumber + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

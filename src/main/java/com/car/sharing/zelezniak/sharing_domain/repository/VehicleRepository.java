package com.car.sharing.zelezniak.sharing_domain.repository;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Year;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

//    @Query("SELECT v FROM Vehicle v WHERE TYPE(v) = :type")
//    Collection<Vehicle> findByVehicleType(@Param("type") Class<? extends Vehicle> type);

    boolean existsByVehicleInformationRegistrationNumber(String registrationNumber);

    Collection<Vehicle> findByVehicleInformationModel(String model);

    Collection<Vehicle> findByVehicleInformationBrand(String brand);

    Collection<Vehicle> findByVehicleInformationRegistrationNumber(String registration);

    Collection<Vehicle> findByVehicleInformationProductionYear(Year year);

    Collection<Vehicle> findByStatus(@Param("status") Vehicle.Status status);
}

package com.vehicle.sharing.zelezniak.vehicle_domain.repository;

import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicle_value_objects.Year;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByVehicleInformationRegistrationNumber(RegistrationNumber registrationNumber);

    Collection<Vehicle> findByVehicleInformationModel(String model);

    Collection<Vehicle> findByVehicleInformationBrand(String brand);

    Collection<Vehicle> findByVehicleInformationRegistrationNumber(RegistrationNumber registration);

    Collection<Vehicle> findByVehicleInformationProductionYear(Year year);

    Collection<Vehicle> findByStatus(@Param("status") Vehicle.Status status);
}

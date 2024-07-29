package com.vehicle.rental.zelezniak.vehicle_domain.repository;

import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicle_value_objects.RegistrationNumber;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicle_value_objects.Year;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface VehicleRepository extends JpaRepository<Vehicle,Long>{

    boolean existsByVehicleInformationRegistrationNumber(RegistrationNumber registrationNumber);

    Page<Vehicle> findByVehicleInformationModel(String model, Pageable pageable);

    Page<Vehicle> findByVehicleInformationBrand(String brand, Pageable pageable);

    Page<Vehicle> findByVehicleInformationRegistrationNumber(RegistrationNumber registration, Pageable pageable);

    Page<Vehicle> findByVehicleInformationProductionYear(Year year, Pageable pageable);

    Page<Vehicle> findByStatus(@Param("status") Vehicle.Status status, Pageable pageable);

    @Query("SELECT v FROM Vehicle v WHERE v.id NOT IN :idSet AND v.status = 'AVAILABLE'")
    Page<Vehicle> findVehiclesByIdNotIn(@Param("idSet") Set<Long> vehiclesIds, Pageable pageable);
}

package com.car.sharing.zelezniak.sharing_domain.repository;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v WHERE v.class.getSimpleName() = :type")
    Collection<Vehicle> findByVehicleType(@Param("type") String type);

//    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH Car c ON v.id = c.id")
//    List<Vehicle> findAllAvailableVehiclesWithCar();




}

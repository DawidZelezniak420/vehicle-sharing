package com.car.sharing.zelezniak.sharing_domain.service;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class VehicleOperations implements VehicleService{

private final VehicleRepository vehicleRepository;

    public Collection<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}

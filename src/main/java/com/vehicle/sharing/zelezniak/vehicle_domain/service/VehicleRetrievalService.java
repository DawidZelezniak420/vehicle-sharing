package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VehicleRetrievalService {

    private final VehicleRepository repository;

    public Collection<Vehicle> findVehiclesByIDs(
            Set<Long> vehiclesIds) {
        Set<Vehicle> vehiclesFromDb = new HashSet<>();
        for (Long id : vehiclesIds) {
            Optional<Vehicle> byId = repository.findById(id);
            if (byId.isEmpty()) {
                throwException(
                        "Vehicle with id: " + id + " does not exists.");
            }
            vehiclesFromDb.add(byId.get());
        }
        return vehiclesFromDb;
    }

    private void throwException(
            String message) {
        throw new IllegalArgumentException(
                message);
    }
}

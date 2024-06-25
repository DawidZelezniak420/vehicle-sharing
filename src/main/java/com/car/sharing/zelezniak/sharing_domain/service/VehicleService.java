package com.car.sharing.zelezniak.sharing_domain.service;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;

import java.util.Collection;

public interface VehicleService {


    Collection<Vehicle> findAll();
}

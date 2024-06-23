package com.car.sharing.zelezniak.sharingdomain.service;

import com.car.sharing.zelezniak.sharingdomain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleOperations implements VehicleService{

private final VehicleRepository vehicleRepository;
}

package com.vehicle.sharing.zelezniak.rent_domain.model.util;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import org.springframework.stereotype.Component;

@Component
public class RentUpdateVisitor {

    public Rent updateRent(
            Rent existingRent,
            Rent newData) {
        return existingRent.toBuilder()
                .rentInformation(newData.getRentInformation())
                .rentStatus(newData.getRentStatus())
                .clients(newData.getClients())
                .vehicles(newData.getVehicles())
                .build();
    }
}

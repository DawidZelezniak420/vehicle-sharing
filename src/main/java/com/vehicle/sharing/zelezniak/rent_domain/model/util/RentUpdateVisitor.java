package com.vehicle.sharing.zelezniak.rent_domain.model.util;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import org.springframework.stereotype.Component;

@Component
public class RentUpdateVisitor {

    public Rent updateRent(
            Rent existingRent,
            Rent newData) {
        return existingRent.toBuilder()
                .totalCost(newData.getTotalCost())
                .rentInformation(newData.getRentInformation())
                .rentStatus(newData.getRentStatus())
                .client(newData.getClient())
                .vehicles(newData.getVehicles())
                .build();
    }
}

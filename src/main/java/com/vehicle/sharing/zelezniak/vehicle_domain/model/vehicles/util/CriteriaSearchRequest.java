package com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CriteriaSearchRequest<T> {

    private String criteriaName;
    private T value;
}

package com.car.sharing.zelezniak.sharing_domain.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum CriteriaType {

    BRAND("brand"),
    MODEL("model"),
    REGISTRATION_NUMBER("registration number"),
    PRODUCTION_YEAR("production year");

    public static CriteriaType getCriteriaFromString(String value) {
        for (CriteriaType criteriaType : CriteriaType.values()) {
            if (criteriaType.getValue().equalsIgnoreCase(value)) {
                return criteriaType;
            }
        }
        throw new IllegalArgumentException("Unknown criteria type " + value);
    }

    private final String value;
}

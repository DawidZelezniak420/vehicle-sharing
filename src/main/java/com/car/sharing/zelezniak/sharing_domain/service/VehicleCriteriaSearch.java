package com.car.sharing.zelezniak.sharing_domain.service;

import com.car.sharing.zelezniak.sharing_domain.model.value_objects.Year;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.repository.VehicleRepository;
import com.car.sharing.zelezniak.util.validation.InputValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class VehicleCriteriaSearch {

    private final Map<CriteriaType,
            Function<Object, Collection<Vehicle>>> criteriaMap
            = initializeCriteriaMap();
    private final VehicleRepository vehicleRepository;
    private final InputValidator validator;


    public <T> Collection<Vehicle> findVehiclesByCriteria(String criteriaType, T value) {
        CriteriaType criteria = CriteriaType.getCriteriaFromString(criteriaType);
        Function<Object, Collection<Vehicle>> queryFunction = criteriaMap.get(criteria);
        return handleExecuteFunction(value, queryFunction);
    }

    private <T> Map<CriteriaType,
            Function<T, Collection<Vehicle>>> initializeCriteriaMap() {
        Map<CriteriaType, Function<T, Collection<Vehicle>>> result = new EnumMap<>(CriteriaType.class);
        result.put(CriteriaType.BRAND, value -> vehicleRepository.findByVehicleInformationBrand((String) value));
        result.put(CriteriaType.MODEL, value -> vehicleRepository.findByVehicleInformationModel((String) value));
        result.put(CriteriaType.REGISTRATION_NUMBER, value -> vehicleRepository.findByVehicleInformationRegistrationNumber((String) value));
        result.put(CriteriaType.PRODUCTION_YEAR, value -> {
            Year year = new Year(getYearValue(value));
            return vehicleRepository.findByVehicleInformationProductionYear(year);
        });
        result.put(CriteriaType.STATUS, value -> {
            String status = (String) value;
            Vehicle.Status s = Vehicle.Status.getStatusFromString(status.toLowerCase());
            return vehicleRepository.findByStatus(s);
        });
        return result;
    }

    private <T> int getYearValue(T value) {
        int yearValue = 0;
        if (value instanceof String s) {
            yearValue = Integer.parseInt((s));
        } else if (value instanceof Number) {
            yearValue = (int) value;
        }
        return yearValue;
    }

    private <T> Collection<Vehicle> handleExecuteFunction(
            T value, Function<T, Collection<Vehicle>> queryFunction) {
        validator.throwExceptionIfObjectIsNull(queryFunction
                ,"The specified criterion is not supported");
        return queryFunction.apply(value);
    }

    @Getter
    @AllArgsConstructor
    enum CriteriaType {

        BRAND("brand"),
        MODEL("model"),
        REGISTRATION_NUMBER("registration number"),
        PRODUCTION_YEAR("production year"),
        STATUS("status");

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
}

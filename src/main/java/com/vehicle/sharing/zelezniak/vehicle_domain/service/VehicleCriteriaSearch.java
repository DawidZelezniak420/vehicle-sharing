package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.vehicle_domain.exception.CriteriaAccessException;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.value_objects.Year;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.repository.VehicleRepository;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleCriteriaSearch {

    private final Map<CriteriaType,
            Function<Object, Collection<Vehicle>>> criteriaMap
            = initializeCriteriaMap();
    private final VehicleRepository vehicleRepository;
    private final InputValidator validator;


    public <T> Collection<Vehicle> findVehiclesByCriteria(String criteriaType, T value) {
        CriteriaType criteria = CriteriaType.getCriteriaFromString(criteriaType);
        checkIfUserCanUseSuchCriteria(criteria);
        Function<Object, Collection<Vehicle>> queryFunction = criteriaMap.get(criteria);
        return handleExecuteFunction(value, queryFunction);
    }

    private void checkIfUserCanUseSuchCriteria(CriteriaType criteria) {
        if (criteria == CriteriaType.REGISTRATION_NUMBER) {
            validateUserHasAdminRole();
        }
    }

    private void validateUserHasAdminRole() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        checkIfUserIsAdmin(isAdmin);
    }

    private void checkIfUserIsAdmin(boolean isAdmin) {
        if (!isAdmin) {
            throwCriteriaAccessException();
        }
    }

    @SneakyThrows
    private void throwCriteriaAccessException() {
        throw new CriteriaAccessException(
                "Access denied: Only admins can search by registration number");
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
                , "The specified criterion is not supported");
        return queryFunction.apply(value);
    }

    @Getter
    @AllArgsConstructor
    private enum CriteriaType {

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

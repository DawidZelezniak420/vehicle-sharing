package com.vehicle.sharing.zelezniak.vehicle_domain.service;

import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import com.vehicle.sharing.zelezniak.vehicle_domain.exception.CriteriaAccessException;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class VehicleCriteriaSearch {

    private final InputValidator validator;
    private final CriteriaSearchExecutor searchExecutor;
    private final Map<CriteriaType,
            Function<Object, Collection<Vehicle>>> criteriaMap;

    public VehicleCriteriaSearch(
            InputValidator validator,
            CriteriaSearchExecutor searchExecutor) {
        this.validator = validator;
        this.searchExecutor = searchExecutor;
        criteriaMap = initializeCriteriaMap();
    }

    public <T> Collection<Vehicle> findVehiclesByCriteria(
            String criteriaType, T value) {
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
                .anyMatch(authority -> authority
                        .getAuthority()
                        .equals("ROLE_ADMIN"));
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

    private <T> Collection<Vehicle> handleExecuteFunction(
            T value, Function<T, Collection<Vehicle>> queryFunction) {
        validator.throwExceptionIfObjectIsNull(queryFunction
                , "The specified criterion is not supported");
        return queryFunction.apply(value);
    }

    private <T> Map<CriteriaType,
            Function<T, Collection<Vehicle>>> initializeCriteriaMap() {
        Map<CriteriaType, Function<T, Collection<Vehicle>>> result = new EnumMap<>(CriteriaType.class);
        assert searchExecutor != null;
        result.put(CriteriaType.BRAND, searchExecutor::findByBrand);
        result.put(CriteriaType.MODEL, searchExecutor::findByModel);
        result.put(CriteriaType.REGISTRATION_NUMBER, searchExecutor::findByRegistrationNumber);
        result.put(CriteriaType.PRODUCTION_YEAR, searchExecutor::findByProductionYear);
        result.put(CriteriaType.STATUS, searchExecutor::findByStatus);
        return result;
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

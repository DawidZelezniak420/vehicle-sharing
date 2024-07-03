package com.car.sharing.zelezniak.util.validation;

import org.springframework.stereotype.Component;

@Component
public class InputValidator implements DataValidator {

    public static final String VEHICLE_ID_NOT_NULL = "Vehicle id can not be a null.";
    public static final String VEHICLE_NOT_NULL = "Vehicle can not be a null.";
    public static final String CLIENT_ID_NOT_NULL = "Client id can not be a null";
    public static final String CLIENT_NOT_NULL = "Client can not be a null";


    @Override
    public <T> void throwExceptionIfObjectIsNull(T input, String message) {
        if (isNull(input)) {
            throwException(message);
        }
    }

    private <T> boolean isNull(T input) {
        return input == null;
    }

    private void throwException(String message) {
        throw new IllegalArgumentException(message);
    }
}

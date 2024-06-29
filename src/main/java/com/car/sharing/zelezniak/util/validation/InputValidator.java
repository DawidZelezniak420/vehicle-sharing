package com.car.sharing.zelezniak.util.validation;

import org.springframework.stereotype.Component;

@Component
public class InputValidator implements DataValidator {

    public static final String VEHICLE_ID_NOT_NULL = "Vehicle id can not be a null.";
    public static final String CLIENT_ID_NOT_NULL = "Client id can not be a null";
    public static final String VEHICLE_NOT_NULL = "Vehicle can not be a null.";
    public static final String CLIENT_NOT_NULL = "Client can not be a null";

    public void throwExceptionIfObjectIsNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}

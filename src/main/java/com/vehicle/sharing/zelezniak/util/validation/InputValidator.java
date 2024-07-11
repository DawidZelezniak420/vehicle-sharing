package com.vehicle.sharing.zelezniak.util.validation;

import org.springframework.stereotype.Component;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;
import static java.util.Objects.isNull;

@Component
public class InputValidator {

    public static final String VEHICLE_ID_NOT_NULL = "Vehicle id" + CAN_NOT_BE_NULL;
    public static final String VEHICLE_NOT_NULL = "Vehicle" + CAN_NOT_BE_NULL;
    public static final String CLIENT_ID_NOT_NULL = "Client id" + CAN_NOT_BE_NULL;
    public static final String CLIENT_NOT_NULL = "Client" + CAN_NOT_BE_NULL;
    public static final String CLIENT_EMAIL_NOT_NULL = "Client email" + CAN_NOT_BE_NULL;
    public static final String RENT_ID_NOT_NULL = "Rent id" + CAN_NOT_BE_NULL;
    public static final String RENT_NOT_NULL = "Rent" + CAN_NOT_BE_NULL;

    public <T> void throwExceptionIfObjectIsNull(T input, String message) {
        if (isNull(input)) {
            throwException(message);
        }
    }

    private void throwException(String message) {
        throw new IllegalArgumentException(message);
    }
}

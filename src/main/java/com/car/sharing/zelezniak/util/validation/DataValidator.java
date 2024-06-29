package com.car.sharing.zelezniak.util.validation;

public interface DataValidator {

    void throwExceptionIfObjectIsNull(Object object, String message);

}

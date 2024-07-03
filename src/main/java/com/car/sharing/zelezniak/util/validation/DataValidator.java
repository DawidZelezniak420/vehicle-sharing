package com.car.sharing.zelezniak.util.validation;

public interface DataValidator {

   <T> void throwExceptionIfObjectIsNull(T input, String message);

}

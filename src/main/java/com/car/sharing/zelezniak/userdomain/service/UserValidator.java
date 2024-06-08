package com.car.sharing.zelezniak.userdomain.service;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {


   public void throwExceptionIfObjectIsNull(Object o){
        if(o == null)
            throw new IllegalArgumentException("Incorrect value");
    }
}

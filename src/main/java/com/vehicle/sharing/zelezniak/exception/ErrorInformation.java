package com.vehicle.sharing.zelezniak.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorInformation {

    private String message;
    private Integer code;
}

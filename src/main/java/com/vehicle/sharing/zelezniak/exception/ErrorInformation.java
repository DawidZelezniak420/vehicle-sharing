package com.vehicle.sharing.zelezniak.exception;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class ErrorInformation {

    private String message;
    private Integer code;
}

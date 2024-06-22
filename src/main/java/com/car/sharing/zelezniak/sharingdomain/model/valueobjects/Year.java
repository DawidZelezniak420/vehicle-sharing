package com.car.sharing.zelezniak.sharingdomain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Embeddable
@Getter
@RequiredArgsConstructor
public class Year {


    @NotBlank(message = "Year can not be blank.")
    private final String year;

    public Year() {
        year = null;
    }
}

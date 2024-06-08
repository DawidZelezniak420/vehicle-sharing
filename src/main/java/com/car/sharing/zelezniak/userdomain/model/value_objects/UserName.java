package com.car.sharing.zelezniak.userdomain.model.value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Embeddable
public class UserName {

    private static final String FIRST_NAME_SIZE_INVALID =  "First name must contains at least 3 characters";
    private static final String LAST_NAME_SIZE_INVALID =  "Last name must contains at least 2 characters";

    @Size(min = 3,message = FIRST_NAME_SIZE_INVALID)
    private final String firstName;

    @Size(min = 2,message = LAST_NAME_SIZE_INVALID)
    private final String lastName;

    public UserName() {
        firstName = "";
        lastName = "";
    }
}

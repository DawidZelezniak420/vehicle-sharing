package com.vehicle.sharing.zelezniak.user_domain.model.client.value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Embeddable
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class UserName {

    private static final String FIRST_NAME_SIZE_INVALID =  "First name must contains at least 3 characters";
    private static final String LAST_NAME_SIZE_INVALID =  "Last name must contains at least 2 characters";

    @Size(min = 3,message = FIRST_NAME_SIZE_INVALID)
    private final String firstName;

    @Size(min = 2,message = LAST_NAME_SIZE_INVALID)
    private final String lastName;
}

package com.car.sharing.zelezniak.userdomain.model.user.value_objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;


@Getter
@Embeddable
@RequiredArgsConstructor
public class UserCredentials {

    private static final String EMAIL_PATTERN = "^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
    private static final String INVALID_EMAIL= "Email address is invalid.";
    private static final String INVALID_EMAIL_PATTERN= "Pattern of email address is invalid.";
    private static final String INVALID_PASSWORD = "Password must contains at least 5 characters.";

    @NotNull(message = INVALID_EMAIL)
    @Pattern(regexp = EMAIL_PATTERN,
            message = INVALID_EMAIL_PATTERN)
    private final String email;

    @NotNull(message = INVALID_PASSWORD)
    @Size(min = 5, message = INVALID_PASSWORD)
    private final String password;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCredentials that = (UserCredentials) o;
        return Objects.equals(email, that.email)
                && Objects.equals(password, that.password);
    }

    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "email= " + email +
                ", password= " + password;
    }
}

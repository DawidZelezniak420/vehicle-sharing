package com.car.sharing.zelezniak.user_domain.model.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email +
                ", password='" + password +
                '}';
    }
}

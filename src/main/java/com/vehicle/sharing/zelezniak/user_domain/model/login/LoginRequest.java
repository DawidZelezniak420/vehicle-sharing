package com.vehicle.sharing.zelezniak.user_domain.model.login;

import lombok.*;

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

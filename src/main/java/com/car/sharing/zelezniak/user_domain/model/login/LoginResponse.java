package com.car.sharing.zelezniak.user_domain.model.login;

import com.car.sharing.zelezniak.user_domain.model.user.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private Client client;
    private String jwt;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "user=" + client +
                ", jwt='" + jwt +
                '}';
    }
}

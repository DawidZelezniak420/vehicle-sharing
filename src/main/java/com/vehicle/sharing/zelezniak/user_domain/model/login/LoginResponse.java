package com.vehicle.sharing.zelezniak.user_domain.model.login;

import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import lombok.*;

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

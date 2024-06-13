package com.car.sharing.zelezniak.userdomain.model.login;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private ApplicationUser user;
    private String jwt;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "user=" + user +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}

package com.car.sharing.zelezniak.userdomain.model.login;

import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private UserCredentials credentials;

}

package com.car.sharing.zelezniak.config;

import com.car.sharing.zelezniak.user_domain.model.user.*;
import com.car.sharing.zelezniak.user_domain.model.user.value_objects.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ClientCreator {

    public Client createClientWithId5() {
        Client client = new Client();
        client.setId(5L);
        client.setName(new UserName("UserFive", "Five"));
        client.setCredentials(new UserCredentials(
                "userfive@gmail.com", "somepass"));
        Address address = new Address(5L, "teststreet", "5",
                "150", "Warsaw", "00-001", "Poland");
        client.setAddress(address);
        client.setRoles(Set.of(new Role("USER")));
        return client;
    }
}

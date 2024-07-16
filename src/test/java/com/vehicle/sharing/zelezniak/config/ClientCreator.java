package com.vehicle.sharing.zelezniak.config;

import com.vehicle.sharing.zelezniak.common_value_objects.City;
import com.vehicle.sharing.zelezniak.common_value_objects.Country;
import com.vehicle.sharing.zelezniak.common_value_objects.Street;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Address;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Role;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserCredentials;
import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.UserName;
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
        Address address = buildAddress();
        client.setAddress(address);
        client.setRoles(Set.of(new Role("USER")));
        return client;
    }

    private Address buildAddress(){
        return Address.builder()
                .id(5L)
                .street(new Street("teststreet"))
                .houseNumber("5")
                .flatNumber("150")
                .city(new City("Warsaw"))
                .postalCode("00-001")
                .country(new Country("Poland"))
                .build();
    }
}

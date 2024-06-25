package com.car.sharing.zelezniak.config;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Car;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Motorcycle;
import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.user_domain.model.user.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestBeans {

    @Bean
    @Scope(scopeName = "prototype")
    public Client createAppUser(){
        return new Client();
    }

    @Bean(name = "car")
    @Scope(scopeName = "prototype")
    public Vehicle createCar(){
        return new Car();
    }

    @Bean(name = "motorcycle")
    @Scope(scopeName = "prototype")
    public Vehicle createMotorcycle(){
        return new Motorcycle();
    }
}

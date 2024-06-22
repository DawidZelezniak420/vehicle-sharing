package com.car.sharing.zelezniak.config;

import com.car.sharing.zelezniak.userdomain.model.user.Client;
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
}

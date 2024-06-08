package com.CarSharing.config;

import com.car.sharing.zelezniak.userdomain.model.ApplicationUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeans {

    @Bean
    public ApplicationUser createAppUser(){
        return new ApplicationUser();
    }
}

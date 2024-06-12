package com.car.sharing.zelezniak.config;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeans {

    @Bean
    public ApplicationUser createAppUser(){
        return new ApplicationUser();
    }
}

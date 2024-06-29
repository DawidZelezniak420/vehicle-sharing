package com.car.sharing.zelezniak.user_domain.service;

import com.car.sharing.zelezniak.user_domain.model.user.Client;

import java.util.Collection;

public interface ClientService {

    Collection<Client> findAll();

    Client findById(Long id);

    void updateClient(Long id, Client newData);

    void delete(Long id);

}

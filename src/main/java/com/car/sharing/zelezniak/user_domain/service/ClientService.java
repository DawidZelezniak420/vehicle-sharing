package com.car.sharing.zelezniak.user_domain.service;

import com.car.sharing.zelezniak.user_domain.model.user.Client;

import java.util.Collection;

public interface ClientService {

    Collection<Client> findAll();

    Client getById(Long id);

    Client update(Long id, Client newData);

    void delete(Long id);
}

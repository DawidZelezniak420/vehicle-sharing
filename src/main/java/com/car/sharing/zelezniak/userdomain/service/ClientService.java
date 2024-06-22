package com.car.sharing.zelezniak.userdomain.service;

import com.car.sharing.zelezniak.userdomain.model.user.Client;

import java.util.Collection;

public interface ClientService {

    Collection<Client> getAll();

    Client getById(Long id);

    Client update(Long id, Client newData);

    void delete(Long id);
}

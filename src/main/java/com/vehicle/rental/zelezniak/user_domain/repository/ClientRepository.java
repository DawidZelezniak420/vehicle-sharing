package com.vehicle.rental.zelezniak.user_domain.repository;

import com.vehicle.rental.zelezniak.user_domain.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCredentialsEmail(String email);

    Client findByCredentialsEmail(String email);

}

package com.vehicle.rental.zelezniak.user_domain.repository;

import com.vehicle.rental.zelezniak.user_domain.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCredentialsEmail(String email);

    Optional<Client> findByCredentialsEmail(String email);

}

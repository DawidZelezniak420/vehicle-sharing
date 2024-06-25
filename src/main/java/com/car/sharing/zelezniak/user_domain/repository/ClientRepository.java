package com.car.sharing.zelezniak.user_domain.repository;

import com.car.sharing.zelezniak.user_domain.model.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCredentialsEmail(String email);

    Client findByCredentialsEmail(String email);

}

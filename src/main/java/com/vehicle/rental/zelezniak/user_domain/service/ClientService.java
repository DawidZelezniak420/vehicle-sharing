package com.vehicle.rental.zelezniak.user_domain.service;

import com.vehicle.rental.zelezniak.user_domain.model.client.Client;
import com.vehicle.rental.zelezniak.util.validation.InputValidator;
import com.vehicle.rental.zelezniak.user_domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;
    private final InputValidator inputValidator;

    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        checkIfNotNull(id, InputValidator.CLIENT_ID_NOT_NULL);
        return findClient(id);
    }

    @Transactional
    public void update(
            Long id, Client newData) {
        checkIfNotNull(id, InputValidator.CLIENT_ID_NOT_NULL);
        checkIfNotNull(newData, InputValidator.CLIENT_NOT_NULL);
        Client clientFromDb = findClient(id);
        validateAndUpdateClient(
                clientFromDb, newData);
    }

    @Transactional
    public void delete(Long id) {
        checkIfNotNull(id, InputValidator.CLIENT_ID_NOT_NULL);
        Client clientToDelete = findClient(id);
        handleDeleteClient(clientToDelete);
    }

    private <T> void checkIfNotNull(
            T value, String message) {
        inputValidator.throwExceptionIfObjectIsNull(
                value, message);
    }

    private Client findClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                        "User with id: " + id + " does not exist."));
    }

    private void validateAndUpdateClient(
            Client clientFromDb,
            Client newData) {
        String clientEmail = clientFromDb.getEmail();
        clientValidator.checkIfUserCanBeUpdated(
                clientEmail, newData);
        clientFromDb.setName(
                newData.getName());
        clientFromDb.setCredentials(
                newData.getCredentials());
        clientFromDb.setAddress(
                newData.getAddress());
        save(clientFromDb);
    }

    private void save(Client client) {
    clientRepository.save(client);
    }

    private void handleDeleteClient(
            Client clientToDelete) {
        removeRoles(clientToDelete);
        clientRepository.delete(clientToDelete);
    }

    private void removeRoles(
            Client userToDelete) {
        userToDelete.setRoles(null);
    }

    public Client findByEmail(String email) {
        checkIfNotNull(email, InputValidator.CLIENT_EMAIL_NOT_NULL);
        return clientRepository.findByCredentialsEmail(
                email);
    }
}


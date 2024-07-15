package com.vehicle.sharing.zelezniak.user_domain.service;

import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.vehicle.sharing.zelezniak.util.validation.InputValidator.*;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;
    private final InputValidator inputValidator;

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        checkIfNotNull(id, CLIENT_ID_NOT_NULL);
        return findClient(id);
    }

    @Transactional
    public void update(
            Long id, Client newData) {
        checkIfNotNull(id, CLIENT_ID_NOT_NULL);
        checkIfNotNull(newData, CLIENT_NOT_NULL);
        Client clientFromDb = findClient(id);
        validateAndUpdateClient(
                clientFromDb, newData);
    }

    @Transactional
    public void delete(Long id) {
        checkIfNotNull(id, CLIENT_ID_NOT_NULL);
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
        checkIfNotNull(email,CLIENT_EMAIL_NOT_NULL);
        return clientRepository.findByCredentialsEmail(
                email);
    }
}


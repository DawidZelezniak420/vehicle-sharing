package com.car.sharing.zelezniak.user_domain.service;

import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.repository.ClientRepository;
import com.car.sharing.zelezniak.util.validation.DataValidator;
import com.car.sharing.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientOperations implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;
    private final DataValidator dataValidator;

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        validateClientId(id);
        return findClient(id);
    }

    @Transactional
    public void updateClient(
            Long id, Client newData) {
        validateClientId(id);
        validateClient(newData);
        Client clientFromDb = findClient(id);
        validateAndUpdateClient(
                clientFromDb, newData);
    }

    @Transactional
    public void delete(Long id) {
        validateClientId(id);
        Client clientToDelete = findClient(id);
        handleDeleteClient(clientToDelete);
    }

    private void validateClientId(Long id) {
        dataValidator.throwExceptionIfObjectIsNull(
                id,InputValidator.CLIENT_ID_NOT_NULL);
    }

    private Client findClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "User with id: " + id + " does not exist."));
    }

    private void validateClient(Client client) {
        dataValidator.throwExceptionIfObjectIsNull(
                client,InputValidator.CLIENT_NOT_NULL);
    }

    private void validateAndUpdateClient(
            Client clientFromDb,
            Client newData) {
        String clientEmail = clientFromDb.getEmail();
        clientValidator.checkIfUserCanBeUpdated(clientEmail, newData);
        clientFromDb.setName(newData.getName());
        clientFromDb.setCredentials(newData.getCredentials());
        clientFromDb.setAddress(newData.getAddress());
        saveClient(clientFromDb);
    }

    private void saveClient(Client client) {
        clientRepository.save(client);
    }

    private void handleDeleteClient(
            Client clientToDelete) {
        removeRoles(clientToDelete);
        clientRepository.delete(clientToDelete);
    }

    private void removeRoles(Client userToDelete) {
        userToDelete.setRoles(null);
    }
}


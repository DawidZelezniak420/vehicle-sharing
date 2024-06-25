package com.car.sharing.zelezniak.user_domain.service;

import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientOperations implements ClientService {

    private final ClientRepository clientRepository;
    private final UserValidator validator;

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client getById(Long id) {
        validator.throwExceptionIfObjectIsNull(id);
        return findUser(id);
    }

    @Transactional
    public Client update(Long id,
                         Client newData) {
        validator.throwExceptionIfObjectIsNull(id);
        validator.throwExceptionIfObjectIsNull(newData);
        Client clientFromDb = findUser(id);
        return validateAndUpdateUser(clientFromDb, newData);
    }

    @Transactional
    public void delete(Long id) {
        validator.throwExceptionIfObjectIsNull(id);
        Client clientToDelete = findUser(id);
        handleDeleteUser(clientToDelete);
    }

    private Client findUser(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "User with id : " + id + " does not exists."));
    }

    private Client validateAndUpdateUser(
            Client clientFromDb,
            Client newData) {
        String userFromDbEmail = clientFromDb.getEmail();
        validator.checkIfUserCanBeUpdated(userFromDbEmail, newData);
        clientFromDb.setName(newData.getName());
        clientFromDb.setCredentials(newData.getCredentials());
        clientFromDb.setAddress(newData.getAddress());
        return clientFromDb;
    }

    private void handleDeleteUser(
            Client clientToDelete) {
        removeRoles(clientToDelete);
        clientRepository.delete(clientToDelete);
    }

    private void removeRoles(Client userToDelete) {
        userToDelete.setRoles(null);
    }
}

package com.vehicle.sharing.zelezniak.rent_domain.service;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.model.util.*;
import com.vehicle.sharing.zelezniak.rent_domain.repository.RentRepository;
import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleRetrievalService;
import com.vehicle.sharing.zelezniak.vehicle_domain.service.VehicleValidator;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;
import static com.vehicle.sharing.zelezniak.util.validation.InputValidator.*;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final InputValidator inputValidator;
    private final EntityManager entityManager;
    private final RentUpdateVisitor updateVisitor;
    private final ClientService clientService;
    private final VehicleValidator vehicleValidator;
    private final VehicleRetrievalService retrievalService;

    @Transactional(readOnly = true)
    public Collection<Rent> findAll() {
        return rentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rent findById(Long id) {
        checkIfNotNull(id,
                RENT_ID_NOT_NULL);
        return findRent(id);
    }

    @Transactional
    public void add(
            RentCreationRequest request) {
        checkIfNotNull(request,
                "Rent creation request"
                        + CAN_NOT_BE_NULL);
        handleAddRent(request);
    }

    @Transactional
    public void updateRent(
            Long id, Rent newData) {
        checkIfNotNull(newData,
                RENT_NOT_NULL);
        Rent existing = findRent(id);
        handleUpdateRent(existing,
                newData);
    }

    private <T> void checkIfNotNull(
            T input, String message) {
        inputValidator.throwExceptionIfObjectIsNull(
                input, message);
    }

    private Rent findRent(Long id) {
        return rentRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Rent with id: " + id + " does not exists."));
    }

    private void handleAddRent(
            RentCreationRequest request) {
        Collection<Vehicle> vehicles = retrievalService.findVehiclesByIDs(
                request.getVehiclesIds());
        vehicleValidator.checkIfVehiclesAreActive(
                vehicles);
        Rent rent = addClientAndVehiclesToRent(
                vehicles, request);
        persistRent(rent);
    }

    private Rent addClientAndVehiclesToRent(
            Collection<Vehicle> vehicles,
            RentCreationRequest request) {
        Rent rent = request.getRent();
        Client client = clientService.findById(
                request.getClientId());
        rent.addVehicles(vehicles);
        rent.addClient(client);
        return rent;
    }

    private void persistRent(Rent rent) {
        entityManager.persist(rent);
        flushEntityManager();
    }

    private void flushEntityManager() {
        entityManager.flush();
    }

    private void handleUpdateRent(
            Rent existing,
            Rent newData) {
        Rent updated = existing.updateRent(
                updateVisitor, newData);
        mergeRent(updated);
    }

    private void mergeRent(Rent updated) {
        entityManager.merge(updated);
        flushEntityManager();
    }
}

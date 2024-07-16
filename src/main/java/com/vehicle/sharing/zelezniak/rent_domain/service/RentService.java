package com.vehicle.sharing.zelezniak.rent_domain.service;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.model.util.RentCreationRequest;
import com.vehicle.sharing.zelezniak.rent_domain.model.util.RentUpdateVisitor;
import com.vehicle.sharing.zelezniak.rent_domain.repository.RentRepository;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.vehicle.sharing.zelezniak.constants.ValidationMessages.CAN_NOT_BE_NULL;
import static com.vehicle.sharing.zelezniak.util.validation.InputValidator.RENT_ID_NOT_NULL;
import static com.vehicle.sharing.zelezniak.util.validation.InputValidator.RENT_NOT_NULL;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final InputValidator inputValidator;
    private final RentUpdateVisitor updateVisitor;
    private final RentBuilder rentBuilder;

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
        Rent rent = rentBuilder.buildRent(
                request);
        save(rent);
    }

    private void save(Rent rent) {
        rentRepository.save(rent);
    }

    private void handleUpdateRent(
            Rent existing,
            Rent newData) {
        Rent updated = existing.updateRent(
                updateVisitor, newData);
        save(updated);
    }
}

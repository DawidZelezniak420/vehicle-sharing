package com.vehicle.sharing.zelezniak.rent_domain.service;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.repository.RentRepository;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import com.vehicle.sharing.zelezniak.vehicle_domain.model.vehicles.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.vehicle.sharing.zelezniak.util.validation.InputValidator.*;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final InputValidator inputValidator;

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
            Rent rent) {
        checkIfNotNull(rent,
                RENT_NOT_NULL);
        handleAddRent(rent);
    }

    @Transactional(readOnly = true)
    public Collection<Rent> findAllByClientId(
            Long id){
        checkIfNotNull(id,
                CLIENT_ID_NOT_NULL);
        return rentRepository.findAllByClientId(id);
    }

    @Transactional(readOnly = true)
    public Collection<Vehicle> findVehiclesByRentId(
            Long id){
        checkIfNotNull(id,
                RENT_ID_NOT_NULL);
        return rentRepository.findVehiclesByRentId(id);
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
            Rent rent) {
        save(rent);
    }

    private void save(Rent rent) {
        rentRepository.save(rent);
    }

}

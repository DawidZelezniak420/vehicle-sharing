package com.vehicle.sharing.zelezniak.rent_domain.service;

import com.vehicle.sharing.zelezniak.rent_domain.model.Rent;
import com.vehicle.sharing.zelezniak.rent_domain.repository.RentRepository;
import com.vehicle.sharing.zelezniak.util.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final InputValidator inputValidator;

    @Transactional(readOnly = true)
    public Collection<Rent> findAll() {
        return rentRepository.findAll();
    }
}

package com.car.sharing.zelezniak.sharing_domain.controller;

import com.car.sharing.zelezniak.sharing_domain.model.vehicles.Vehicle;
import com.car.sharing.zelezniak.sharing_domain.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/")
    public Collection<Vehicle> findAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle findById(
            @PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(
            @RequestBody @Validated
            Vehicle vehicle) {
        vehicleService.add(vehicle);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable Long id,
                       @Validated @RequestBody
                       Vehicle newData) {
        vehicleService.update(id, newData);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehicleService.delete(id);
    }

    @GetMapping("/criteria")
    public <T> Collection<Vehicle> findByCriteria(
                 @RequestParam String criteriaName,
                    @RequestParam T value) {
        return vehicleService.findByCriteria(
                         criteriaName, value);
    }
}

package com.vehicle.rental.zelezniak.vehicle_domain.service.vehicle_update;

import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Car;
import com.vehicle.rental.zelezniak.vehicle_domain.model.vehicles.Motorcycle;
import org.springframework.stereotype.Component;

/**
 *Allows to select a vehicle update strategy based on vehicle type.
 */
@Component
public class VehicleUpdateStrategyFactory {

    public <T>VehicleUpdateStrategy getStrategy(Class<T> type) {
        if (type.equals(Car.class)) {
            return new CarUpdateStrategy();
        } else if (type.equals(Motorcycle.class)) {
            return new MotorcycleUpdateStrategy();
        } else throw new IllegalArgumentException("Unsupported vehicle type");
    }
}

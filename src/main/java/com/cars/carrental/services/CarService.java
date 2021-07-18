package com.cars.carrental.services;

import com.cars.carrental.models.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> findAll();

    Optional<Car> findById(String id);

    Car saveOrUpdate(Car car);

    void delete(String id);

    Car findByBrand(String brand);

    boolean exists(String id);
}
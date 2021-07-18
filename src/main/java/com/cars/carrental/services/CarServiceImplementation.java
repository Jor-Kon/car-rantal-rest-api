package com.cars.carrental.services;

import com.cars.carrental.models.Car;
import com.cars.carrental.repositories.CarRepository;
import com.cars.carrental.services.CarService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImplementation implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> findById(String id) {
        return carRepository.findById(id);
    }

    @Override
    public Car saveOrUpdate(Car car) {
        return carRepository.save(car);
    }

    @Override
    public void delete(String id) {
        carRepository.deleteById(id);
    }

    @Override
    public Car findByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    @Override
    public boolean exists(String id) {
        return carRepository.existsById(id);
    }
}
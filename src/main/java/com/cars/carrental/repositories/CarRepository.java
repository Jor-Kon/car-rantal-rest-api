package com.cars.carrental.repositories;

import com.cars.carrental.models.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarRepository extends MongoRepository<Car, String> {

    Car findByBrand(String brand);
   
    void deleteAll();
}
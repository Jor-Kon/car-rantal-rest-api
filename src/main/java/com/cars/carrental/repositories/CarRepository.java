package com.cars.carrental.repositories;

import com.cars.carrental.models.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CarRepository extends MongoRepository<Car, String> {

    Car findByBrand(String brand);
   
}
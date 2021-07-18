package com.cars.carrental.controllers;

import com.cars.carrental.models.Car;
import com.cars.carrental.services.CarService;
import com.cars.carrental.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/cars")
public class CarRestController {
    @Autowired
    private CarService carService;

    @GetMapping(value = "/")
    public List<Car> getAllCars() {
        return ObjectMapperUtils.mapAll(carService.findAll(), Car.class);
    }

    @GetMapping(value = "/{id}")
    public Car getCar(@PathVariable("id") String id) {
        return ObjectMapperUtils.map(carService.findById(id).get(), Car.class);
    }

    @GetMapping(value = "/brand/{brand}")
    public Car getCarByBrand(@PathVariable("brand") String brand) {
        return ObjectMapperUtils.map(carService.findByBrand(brand), Car.class);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        carService.saveOrUpdate(ObjectMapperUtils.map(car, Car.class));
        return new ResponseEntity("Car added", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable("id") String id) {
        if (carService.exists(id)) {
            carService.delete(id);
            return new ResponseEntity("Car deleted", HttpStatus.OK);
        }
        else
        return new ResponseEntity("Car does not exist in database", HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/edit/{id}")
    public ResponseEntity<?> editCar(@PathVariable("id") String id, @RequestBody Car editedCar) {
        if (carService.exists(id)) {
            Car car = ObjectMapperUtils.map(carService.findById(id).get(), Car.class);
            // boolean isRented = car.getIsRented();
            // car = editedCar;
            // car.setId(id);
            // car.setIsRented(isRented);
            car.setBrand(editedCar.getBrand());
            car.setModel(editedCar.getModel());
            car.setYearOfProduction(editedCar.getYearOfProduction());
            car.setRentalCost(editedCar.getRentalCost());
            carService.saveOrUpdate(ObjectMapperUtils.map(car, Car.class));
            return new ResponseEntity("Car edited", HttpStatus.CREATED);
        }
        else
        return new ResponseEntity("Car does not exist in database", HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/rent/{id}")
    public ResponseEntity<?> rentCar(@PathVariable("id") String id) {
        if (carService.exists(id)) {
            Car car = ObjectMapperUtils.map(carService.findById(id).get(), Car.class);
            if (car.getIsRented() == true) {
                return new ResponseEntity("Car is already occupied", HttpStatus.FORBIDDEN);
            }
            else {
                car.setIsRented(true);
                carService.saveOrUpdate(ObjectMapperUtils.map(car, Car.class));
                return new ResponseEntity("Car rented successfully", HttpStatus.OK);
            }
        }
        else
        return new ResponseEntity("Car does not exist in database", HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/return/{id}")
    public ResponseEntity<?> returnCar(@PathVariable("id") String id) {
        if (carService.exists(id)) {
            Car car = ObjectMapperUtils.map(carService.findById(id).get(), Car.class);
            if (car.getIsRented() == false) {
                return new ResponseEntity("Car is not occupied", HttpStatus.FORBIDDEN);
            }
            else {
                car.setIsRented(false);
                carService.saveOrUpdate(ObjectMapperUtils.map(car, Car.class));
                return new ResponseEntity("Car returned successfully", HttpStatus.OK);
            }
        }
        else
        return new ResponseEntity("Car does not exist in database", HttpStatus.CONFLICT);
    }
}
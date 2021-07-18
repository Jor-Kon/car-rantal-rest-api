package com.cars.carrental.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "cars")
public class Car {
    @Id
    private String id;
    private String brand;
    private String model;
    private int yearOfProduction;
    private float rentalCost;
    private boolean isRented;

    public Car() {
    }

    public Car(String brand, String model, int yearOfProduction, float rentalCost) {
        this.brand = brand;
        this.model = model;
        this.yearOfProduction = yearOfProduction;
        this.rentalCost = rentalCost;
        this.isRented = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public float getRentalCost() {
        return rentalCost;
    }

    public void setRentalCost(float rentalCost) {
        this.rentalCost = rentalCost;
    }

    public boolean getIsRented() {
        return isRented;
    }

    public void setIsRented(boolean isRented) {
        this.isRented = isRented;
    }
}
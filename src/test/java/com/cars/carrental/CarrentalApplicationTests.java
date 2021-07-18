package com.cars.carrental;

import com.cars.carrental.models.Car;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarrentalApplicationTests {

	public CarrentalApplicationTests() {}

	@Autowired
    CarrentalApplicationTests(MongoClient mongoClient) {
        createCarCollectionIfNotPresent(mongoClient);
    }

	@Autowired
    private TestRestTemplate rest;

	private String URL = "http://localhost:8080/api/cars";

	private Car car;
	private Car carToDelete;
	private Car carToReturn;
	private Car carOccupied;


	@Before
	public void init() {
		this.car = new Car("Brand1", "Model1", 1999, 127.10f);
		this.car.setId("id1");

		this.carToDelete = new Car("Brand2", "Model2", 2005, 250.00f);
		this.carToDelete.setId("id2");

		this.carToReturn = new Car("Brand3", "Model3", 2014, 500.99f);
		this.carToReturn.setId("id3");
		this.carToReturn.setIsRented(true);

		this.carOccupied = new Car("Brand4", "Model4", 2014, 500.99f);
		this.carOccupied.setId("id4");
		this.carOccupied.setIsRented(true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Car> entity = new HttpEntity<>(car, headers);
		this.rest.postForEntity(URL + "/add", entity, String.class);

		entity = new HttpEntity<>(carToDelete, headers);
		this.rest.postForEntity(URL + "/add", entity, String.class);

		entity = new HttpEntity<>(carToReturn, headers);
		this.rest.postForEntity(URL + "/add", entity, String.class);

		entity = new HttpEntity<>(carOccupied, headers);
		this.rest.postForEntity(URL + "/add", entity, String.class);
	}

	@After
	public void tearDown() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("car", headers);
		this.rest.exchange(URL + "/delete/id1", HttpMethod.DELETE, entity, String.class);
		this.rest.exchange(URL + "/delete/id2", HttpMethod.DELETE, entity, String.class);
		this.rest.exchange(URL + "/delete/id3", HttpMethod.DELETE, entity, String.class);
		this.rest.exchange(URL + "/delete/id4", HttpMethod.DELETE, entity, String.class);
	}

	@Test
	public void testAddCar() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Car> entity = new HttpEntity<>(car, headers);
		ResponseEntity<String> result = this.rest.postForEntity(URL + "/add", entity, String.class);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getBody()).isEqualTo("Car added");
	}

	@Test
	public void testDeleteCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("car", headers);
		ResponseEntity<String> result = this.rest.exchange(URL + "/delete/id2", HttpMethod.DELETE, entity, String.class);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Car deleted");
	}

	@Test
	public void testEditCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Car car = new Car("Edited Brand", "Edited Model", 2005, 140.10f);
		HttpEntity<Car> entity = new HttpEntity<>(car, headers);
		ResponseEntity<String> result = this.rest.exchange(URL + "/edit/id1", HttpMethod.PUT, entity, String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Car edited");
	}

	@Test
	public void testEditNonexistingCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Car car = new Car("Edited Brand", "Edited Model", 2005, 140.10f);
		HttpEntity<Car> entity = new HttpEntity<>(car, headers);
		ResponseEntity<String> result = this.rest.exchange(URL + "/edit/id20", HttpMethod.PUT, entity, String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(result.getBody()).isEqualTo("Car does not exist in database");
	}

	@Test
	public void testRentCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("car", headers);
		ResponseEntity<String> result = this.rest.exchange(URL + "/rent/id1", HttpMethod.PUT, entity, String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Car rented successfully");
	}

	@Test
	public void testRentOccupiedCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("car", headers);
		ResponseEntity<String> result = this.rest.exchange(URL + "/rent/id4", HttpMethod.PUT, entity, String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(result.getBody()).isEqualTo("Car is already occupied");
	}

	@Test
	public void testReturnCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("car", headers);
		ResponseEntity<String> result = this.rest.exchange(URL + "/return/id3", HttpMethod.PUT, entity, String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Car returned successfully");
	}

	@Test
	public void testReturnUnoccupiedCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("car", headers);
		ResponseEntity<String> result = this.rest.exchange(URL + "/return/id1", HttpMethod.PUT, entity, String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(result.getBody()).isEqualTo("Car is not occupied");
	}

	private void createCarCollectionIfNotPresent(MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase("test1");
        if (!db.listCollectionNames().into(new ArrayList<>()).contains("cars1"))
            db.createCollection("cars1");
    }
}

package com.abg.foo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class Car {
	String name;
	int year;
	public Car(String name, int year) {
		super();
		this.name = name;
		this.year = year;
	}
}

@RestController
public class CarController {
	
	List<Car> carsList;
	
	@Value(value = "${bar.service.base.url}")
	String barServiceURL;
	
	@Value(value = "${external.service.url}")
	String externalServiceURL;
	
	public CarController() {
		carsList = new ArrayList<Car>();
		carsList.add(new Car("ford", 2019));
		carsList.add(new Car("ferrari",2019));
	}
	
    @GetMapping("/static") 
    List<Car> findAll() {
    	System.out.println("/static request");
    	return carsList;
    }
    
    @GetMapping("/external") 
    ResponseEntity<String> callExternalAPI() {
    	System.out.println("/external request");
    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(externalServiceURL, String.class);
    	
    	return response;
    }
    
    @GetMapping("/data") 
    ResponseEntity<String> callBarService() {
    	System.out.println("/data request");
    	RestTemplate restTemplate = new RestTemplate();
    	String barResourceUrl
    	  = barServiceURL + "/data";
    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(barResourceUrl, String.class);
    	
    	return response;
    }
}

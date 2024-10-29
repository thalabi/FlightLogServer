package com.kerneldc.flightlogserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlightLogServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FlightLogServerApplication.class, args);
	}
}

package com.kerneldc.flightlogserver.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.repository.AirportRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/externalAirportController")
@RequiredArgsConstructor
public class ExternalAirportController {

	private final AirportRepository airportRepository;

	@GetMapping("/getListOfAirportIdentifiers")
	public ResponseEntity<Map<String, List<String>>> getListOfAirportIdentifiers() {
		return ResponseEntity.ok(Map.of("identifiers", airportRepository.getListOfIdentifiers()));
    }

}

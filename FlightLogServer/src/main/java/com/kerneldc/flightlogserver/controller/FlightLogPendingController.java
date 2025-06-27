package com.kerneldc.flightlogserver.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.service.FlightLogPendingService;
import com.kerneldc.flightlogserver.service.FlightLogPendingService.FlightLogPendingAddRequest;

import jakarta.validation.Valid;
//import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/protected/flightLogPendingController")
@RequiredArgsConstructor
public class FlightLogPendingController {

	private final FlightLogPendingService flightLogPendingService;

	@PostMapping("/addFlightLogPending")
	public ResponseEntity<String> addFlightLogPending(@Valid @RequestBody FlightLogPendingAddRequest flightLogPendingAddRequest) {
		flightLogPendingService.addFlightLog(flightLogPendingAddRequest);
		return ResponseEntity.ok(StringUtils.EMPTY);
    }

}

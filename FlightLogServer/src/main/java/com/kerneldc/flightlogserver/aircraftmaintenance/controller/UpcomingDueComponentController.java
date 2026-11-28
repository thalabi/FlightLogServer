package com.kerneldc.flightlogserver.aircraftmaintenance.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.aircraftmaintenance.service.AircrafDetailsService;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.UpcomingDueComponentService;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.AircrafDetailsService.Tsn;
import com.kerneldc.flightlogserver.exception.ApplicationException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/protected/upcomingDueComponentController")
@RequiredArgsConstructor
@Slf4j
public class UpcomingDueComponentController {

	private static final String LOG_BEGIN = "Begin ...";
	private static final String LOG_END = "End ...";

	private final AircrafDetailsService aircrafDetailsService;

	private final UpcomingDueComponentService upcomingDueComponentService;

	@GetMapping(value = "/getAirtime")
	public ResponseEntity<Tsn> getAirtime() throws ApplicationException {
		var tsn = aircrafDetailsService.getAircraftTsn("C-GQGD");
		return ResponseEntity.ok(tsn);
	}

	@GetMapping(value = "/emailUpcomingDueComponents")
	public ResponseEntity<Boolean> emailUpcomingDueComponents(
			@RequestParam(value = "dueDate") @DateTimeFormat(iso = ISO.DATE_TIME) @Valid Date toDueDate,
			@RequestParam(value = "hrsDue") @Valid Float toHrsDue,
			@RequestParam(value = "emailAddress") @Valid String emailAddress
			) throws ApplicationException {

		LOGGER.info(LOG_BEGIN);
		
		LOGGER.info("toDueDate [{}], toHrsDue [{}], emailAddress [{}]", toDueDate, toHrsDue, emailAddress);
		
		upcomingDueComponentService.checkUpcomingDueComponentsNotification(toDueDate, toHrsDue);
		
		LOGGER.info(LOG_END);
		
		return ResponseEntity.ok(true);
	}

}

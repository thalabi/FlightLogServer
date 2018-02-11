package com.kerneldc.flightlogserver.domain;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "flightLogs")
public class FlightLogResource extends ResourceSupport {

	@JsonUnwrapped
	private FlightLog flightLog;
	
}

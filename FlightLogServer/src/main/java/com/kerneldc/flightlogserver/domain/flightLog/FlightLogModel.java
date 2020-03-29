package com.kerneldc.flightlogserver.domain.flightLog;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "flightLogs")
public class FlightLogModel extends RepresentationModel<FlightLogModel> {

	@JsonUnwrapped
	private FlightLog flightLog;
	
}

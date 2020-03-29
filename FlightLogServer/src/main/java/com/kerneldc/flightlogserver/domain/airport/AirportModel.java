package com.kerneldc.flightlogserver.domain.airport;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "airports")
public class AirportModel extends RepresentationModel<AirportModel> {

	@JsonUnwrapped
	private Airport airport;
	
}

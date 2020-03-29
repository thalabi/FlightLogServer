package com.kerneldc.flightlogserver.aircraftmaintenance.domain.part;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "parts")
public class PartModel extends RepresentationModel<PartModel> {

	@JsonUnwrapped
	private Part part;
	
}

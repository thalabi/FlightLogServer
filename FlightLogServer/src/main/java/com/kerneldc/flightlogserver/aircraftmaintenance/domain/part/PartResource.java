package com.kerneldc.flightlogserver.aircraftmaintenance.domain.part;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "parts")
public class PartResource extends ResourceSupport {

	@JsonUnwrapped
	private Part part;
	
}

package com.kerneldc.flightlogserver.domain;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "airports")
public class AirportResource extends ResourceSupport {

	@JsonUnwrapped
	private Airport airport;
	
}

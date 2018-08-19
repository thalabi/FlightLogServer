package com.kerneldc.flightlogserver.domain.pilot;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "pilots")
public class PilotResource extends ResourceSupport {

	@JsonUnwrapped
	private Pilot pilot;
	
}

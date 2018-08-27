package com.kerneldc.flightlogserver.domain.significantEvent;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "significantEvents")
public class SignificantEventResource extends ResourceSupport {

	@JsonUnwrapped
	private SignificantEvent significantEvent;
	
}

package com.kerneldc.flightlogserver.domain.significantEvent;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "significantEvents")
public class SignificantEventModel extends RepresentationModel<SignificantEventModel> {

	@JsonUnwrapped
	private SignificantEvent significantEvent;
	
}

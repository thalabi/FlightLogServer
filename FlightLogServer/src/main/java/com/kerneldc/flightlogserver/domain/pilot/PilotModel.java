package com.kerneldc.flightlogserver.domain.pilot;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "pilots")
public class PilotModel extends RepresentationModel<PilotModel> {

	@JsonUnwrapped
	private Pilot pilot;
	
}

package com.kerneldc.flightlogserver.domain.registration;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "registrations")
public class RegistrationModel extends RepresentationModel<RegistrationModel> {

	@JsonUnwrapped
	private Registration registration;
	
}

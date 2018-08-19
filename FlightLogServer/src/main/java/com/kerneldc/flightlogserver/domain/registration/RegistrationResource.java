package com.kerneldc.flightlogserver.domain.registration;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "registrations")
public class RegistrationResource extends ResourceSupport {

	@JsonUnwrapped
	private Registration registration;
	
}

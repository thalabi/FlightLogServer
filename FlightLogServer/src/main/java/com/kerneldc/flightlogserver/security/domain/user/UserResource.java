package com.kerneldc.flightlogserver.security.domain.user;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "users")
public class UserResource extends ResourceSupport {

	@JsonUnwrapped
	private User user;
	
}

package com.kerneldc.flightlogserver.security.domain.user;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "users")
public class UserModel extends RepresentationModel<UserModel> {

	@JsonUnwrapped
	private User user;
	
}

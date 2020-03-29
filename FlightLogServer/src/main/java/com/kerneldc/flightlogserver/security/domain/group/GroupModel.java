package com.kerneldc.flightlogserver.security.domain.group;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "groups")
public class GroupModel extends RepresentationModel<GroupModel> {

	@JsonUnwrapped
	private Group group;
	
}

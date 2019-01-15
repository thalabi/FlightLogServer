package com.kerneldc.flightlogserver.security.domain.group;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "groups")
public class GroupResource extends ResourceSupport {

	@JsonUnwrapped
	private Group group;
	
}

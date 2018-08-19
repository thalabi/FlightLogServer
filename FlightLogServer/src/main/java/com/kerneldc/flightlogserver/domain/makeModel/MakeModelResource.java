package com.kerneldc.flightlogserver.domain.makeModel;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "makeModels")
public class MakeModelResource extends ResourceSupport {

	@JsonUnwrapped
	private MakeModel makeModel;
	
}

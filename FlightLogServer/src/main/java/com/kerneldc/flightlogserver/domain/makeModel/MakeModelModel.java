package com.kerneldc.flightlogserver.domain.makeModel;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "makeModels")
public class MakeModelModel extends RepresentationModel<MakeModelModel> {

	@JsonUnwrapped
	private MakeModel makeModel;
	
}

package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.Set;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistoryModel;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "components")
public class ComponentModel extends RepresentationModel<ComponentModel> {

	@JsonUnwrapped
	private Component component;
	
	private Part part;
	@JsonProperty("componentHistorySet")
	private Set<ComponentHistoryModel> componentHistoryResourceSet;
}

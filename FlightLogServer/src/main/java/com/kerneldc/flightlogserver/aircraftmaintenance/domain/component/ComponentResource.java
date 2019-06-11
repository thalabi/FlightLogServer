package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.Set;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistoryResource;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "components")
public class ComponentResource extends ResourceSupport {

	@JsonUnwrapped
	private Component component;
	
	private Part part;
	@JsonProperty("componentHistorySet")
	private Set<ComponentHistoryResource> componentHistoryResourceSet;
}

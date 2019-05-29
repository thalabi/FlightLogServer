package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.Set;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Relation(collectionRelation = "components")
public class ComponentResource extends ResourceSupport {

	@JsonUnwrapped
	private Component component;
	
//	private String partName;
//	private String partDescription;
	
	private Part part;
	private Set<ComponentHistory> componentHistorySet;
}

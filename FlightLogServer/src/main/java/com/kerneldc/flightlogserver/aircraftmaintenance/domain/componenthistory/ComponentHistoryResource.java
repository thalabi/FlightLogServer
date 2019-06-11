package com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ComponentHistoryResource extends ResourceSupport {

	@JsonUnwrapped
	private ComponentHistory componentHistory;
	
	private Part part;
}

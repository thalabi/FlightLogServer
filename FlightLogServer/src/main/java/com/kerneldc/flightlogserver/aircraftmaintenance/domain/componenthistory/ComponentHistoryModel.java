package com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ComponentHistoryModel extends RepresentationModel<ComponentHistoryModel> {

	@JsonUnwrapped
	private ComponentHistory componentHistory;
	
	private Part part;
}

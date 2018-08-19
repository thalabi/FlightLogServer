package com.kerneldc.flightlogserver.domain.flightLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.FlightLogController;

@Component
public class FlightLogResourceAssembler extends ResourceAssemblerSupport<FlightLog, FlightLogResource> {

	@Autowired
	private EntityLinks repositoryEntityLinks;
	
	public FlightLogResourceAssembler() {
		super(FlightLogController.class, FlightLogResource.class);
	}

	@Override
	public FlightLogResource toResource(FlightLog flightLog) {
		Link link = repositoryEntityLinks.linkToSingleResource(flightLog);
		FlightLogResource flightLogResource = new FlightLogResource();
		flightLogResource.setFlightLog(flightLog);
		flightLogResource.add(link);
		flightLogResource.add(link.withSelfRel());
		return flightLogResource;
	}
	
	@Override
	public FlightLogResource instantiateResource(FlightLog flightLog) {
		return new FlightLogResource();
	}
}

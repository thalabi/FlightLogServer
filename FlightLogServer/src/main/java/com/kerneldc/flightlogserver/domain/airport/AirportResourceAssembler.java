package com.kerneldc.flightlogserver.domain.airport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.AirportController;

@Component
public class AirportResourceAssembler extends ResourceAssemblerSupport<Airport, AirportResource> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public AirportResourceAssembler() {
		super(AirportController.class, AirportResource.class);
	}

	@Override
	public AirportResource toResource(Airport airport) {
		Link link = repositoryEntityLinks.linkToSingleResource(airport);
		AirportResource airportResource = new AirportResource();
		airportResource.setAirport(airport);
		airportResource.add(link);
		airportResource.add(link.withSelfRel());
		return airportResource;
	}
	
	@Override
	public AirportResource instantiateResource(Airport airport) {
		return new AirportResource();
	}
}

package com.kerneldc.flightlogserver.domain.airport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.AirportController;

@Component
public class AirportModelAssembler extends RepresentationModelAssemblerSupport <Airport, AirportModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public AirportModelAssembler() {
		super(AirportController.class, AirportModel.class);
	}

	@Override
	public AirportModel toModel(Airport airport) {
		Link link = repositoryEntityLinks.linkToItemResource(airport, Airport.idExtractor);
		AirportModel airportModel = new AirportModel();
		airportModel.setAirport(airport);
		airportModel.add(link);
		airportModel.add(link.withSelfRel());
		return airportModel;
	}
}

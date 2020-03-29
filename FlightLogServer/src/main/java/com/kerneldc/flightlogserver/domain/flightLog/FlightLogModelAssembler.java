package com.kerneldc.flightlogserver.domain.flightLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.FlightLogController;

@Component
public class FlightLogModelAssembler extends RepresentationModelAssemblerSupport<FlightLog, FlightLogModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public FlightLogModelAssembler() {
		super(FlightLogController.class, FlightLogModel.class);
	}

	@Override
	public FlightLogModel toModel(FlightLog flightLog) {
		Link link = repositoryEntityLinks.linkToItemResource(flightLog, FlightLog.idExtractor);
		FlightLogModel flightLogModel = new FlightLogModel();
		flightLogModel.setFlightLog(flightLog);
		flightLogModel.add(link);
		flightLogModel.add(link.withSelfRel());
		return flightLogModel;
	}
}

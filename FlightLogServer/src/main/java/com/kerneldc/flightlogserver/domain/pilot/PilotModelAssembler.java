package com.kerneldc.flightlogserver.domain.pilot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.PilotController;

@Component
public class PilotModelAssembler extends RepresentationModelAssemblerSupport <Pilot, PilotModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public PilotModelAssembler() {
		super(PilotController.class, PilotModel.class);
	}

	@Override
	public PilotModel toModel(Pilot pilot) {
		Link link = repositoryEntityLinks.linkToItemResource(pilot, Pilot.idExtractor);
		PilotModel pilotModel = new PilotModel();
		pilotModel.setPilot(pilot);
		pilotModel.add(link);
		pilotModel.add(link.withSelfRel());
		return pilotModel;
	}
}

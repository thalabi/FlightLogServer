package com.kerneldc.flightlogserver.domain.pilot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.PilotController;

@Component
public class PilotResourceAssembler extends ResourceAssemblerSupport<Pilot, PilotResource> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public PilotResourceAssembler() {
		super(PilotController.class, PilotResource.class);
	}

	@Override
	public PilotResource toResource(Pilot pilot) {
		Link link = repositoryEntityLinks.linkToSingleResource(pilot);
		PilotResource pilotResource = new PilotResource();
		pilotResource.setPilot(pilot);
		pilotResource.add(link);
		pilotResource.add(link.withSelfRel());
		return pilotResource;
	}
	
	@Override
	public PilotResource instantiateResource(Pilot pilot) {
		return new PilotResource();
	}
}

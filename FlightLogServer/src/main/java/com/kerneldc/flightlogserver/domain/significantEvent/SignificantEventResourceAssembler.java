package com.kerneldc.flightlogserver.domain.significantEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.SignificantEventController;

@Component
public class SignificantEventResourceAssembler extends ResourceAssemblerSupport<SignificantEvent, SignificantEventResource> {

	@Autowired
	private EntityLinks repositoryEntityLinks;
	
	public SignificantEventResourceAssembler() {
		super(SignificantEventController.class, SignificantEventResource.class);
	}

	@Override
	public SignificantEventResource toResource(SignificantEvent significantEvent) {
		Link link = repositoryEntityLinks.linkToSingleResource(significantEvent);
		SignificantEventResource significantEventResource = new SignificantEventResource();
		significantEventResource.setSignificantEvent(significantEvent);
		significantEventResource.add(link);
		significantEventResource.add(link.withSelfRel());
		return significantEventResource;
	}
	
	@Override
	public SignificantEventResource instantiateResource(SignificantEvent significantEvent) {
		return new SignificantEventResource();
	}
}

package com.kerneldc.flightlogserver.domain.significantEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.SignificantEventController;

@Component
public class SignificantEventModelAssembler extends RepresentationModelAssemblerSupport <SignificantEvent, SignificantEventModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public SignificantEventModelAssembler() {
		super(SignificantEventController.class, SignificantEventModel.class);
	}

	@Override
	public SignificantEventModel toModel(SignificantEvent significantEvent) {
		Link link = repositoryEntityLinks.linkToItemResource(significantEvent, SignificantEvent.idExtractor);
		SignificantEventModel significantEventModel = new SignificantEventModel();
		significantEventModel.setSignificantEvent(significantEvent);
		significantEventModel.add(link);
		significantEventModel.add(link.withSelfRel());
		return significantEventModel;
	}
}

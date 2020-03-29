package com.kerneldc.flightlogserver.domain.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.RegistrationController;

@Component
public class RegistrationModelAssembler extends RepresentationModelAssemblerSupport <Registration, RegistrationModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public RegistrationModelAssembler() {
		super(RegistrationController.class, RegistrationModel.class);
	}

	@Override
	public RegistrationModel toModel(Registration registration) {
		Link link = repositoryEntityLinks.linkToItemResource(registration, Registration.idExtractor);
		RegistrationModel registrationModel = new RegistrationModel();
		registrationModel.setRegistration(registration);
		registrationModel.add(link);
		registrationModel.add(link.withSelfRel());
		return registrationModel;
	}
}

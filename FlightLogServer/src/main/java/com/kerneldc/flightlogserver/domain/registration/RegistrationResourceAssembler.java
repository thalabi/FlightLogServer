package com.kerneldc.flightlogserver.domain.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.RegistrationController;

@Component
public class RegistrationResourceAssembler extends ResourceAssemblerSupport<Registration, RegistrationResource> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public RegistrationResourceAssembler() {
		super(RegistrationController.class, RegistrationResource.class);
	}

	@Override
	public RegistrationResource toResource(Registration registration) {
		Link link = repositoryEntityLinks.linkToSingleResource(registration);
		RegistrationResource registrationResource = new RegistrationResource();
		registrationResource.setRegistration(registration);
		registrationResource.add(link);
		registrationResource.add(link.withSelfRel());
		return registrationResource;
	}
	
	@Override
	public RegistrationResource instantiateResource(Registration registration) {
		return new RegistrationResource();
	}
}

package com.kerneldc.flightlogserver.aircraftmaintenance.domain.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.aircraftmaintenance.controller.PartController;

@Component
public class PartResourceAssembler extends ResourceAssemblerSupport<Part, PartResource> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public PartResourceAssembler() {
		super(PartController.class, PartResource.class);
	}

	@Override
	public PartResource toResource(Part part) {
		Link link = repositoryEntityLinks.linkToSingleResource(part);
		PartResource partResource = new PartResource();
		partResource.setPart(part);
		partResource.add(link);
		partResource.add(link.withSelfRel());
		return partResource;
	}
	
	@Override
	public PartResource instantiateResource(Part part) {
		return new PartResource();
	}
}

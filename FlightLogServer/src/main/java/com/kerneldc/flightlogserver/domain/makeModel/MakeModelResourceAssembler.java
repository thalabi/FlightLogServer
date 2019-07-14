package com.kerneldc.flightlogserver.domain.makeModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.MakeModelController;

@Component
public class MakeModelResourceAssembler extends ResourceAssemblerSupport<MakeModel, MakeModelResource> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public MakeModelResourceAssembler() {
		super(MakeModelController.class, MakeModelResource.class);
	}

	@Override
	public MakeModelResource toResource(MakeModel makeModel) {
		Link link = repositoryEntityLinks.linkToSingleResource(makeModel);
		MakeModelResource makeModelResource = new MakeModelResource();
		makeModelResource.setMakeModel(makeModel);
		makeModelResource.add(link);
		makeModelResource.add(link.withSelfRel());
		return makeModelResource;
	}
	
	@Override
	public MakeModelResource instantiateResource(MakeModel makeModel) {
		return new MakeModelResource();
	}
}

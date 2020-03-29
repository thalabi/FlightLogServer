package com.kerneldc.flightlogserver.domain.makeModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.controller.MakeModelController;

@Component
public class MakeModelModelAssembler extends RepresentationModelAssemblerSupport <MakeModel, MakeModelModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public MakeModelModelAssembler() {
		super(MakeModelController.class, MakeModelModel.class);
	}

	@Override
	public MakeModelModel toModel(MakeModel makeModel) {
		Link link = repositoryEntityLinks.linkToItemResource(makeModel, MakeModel.idExtractor);
		MakeModelModel makeModelModel = new MakeModelModel();
		makeModelModel.setMakeModel(makeModel);
		makeModelModel.add(link);
		makeModelModel.add(link.withSelfRel());
		return makeModelModel;
	}
}

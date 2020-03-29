package com.kerneldc.flightlogserver.aircraftmaintenance.domain.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.aircraftmaintenance.controller.PartController;

@Component
public class PartModelAssembler extends RepresentationModelAssemblerSupport<Part, PartModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public PartModelAssembler() {
		super(PartController.class, PartModel.class);
	}

	@Override
	public PartModel toModel(Part part) {
		Link link = repositoryEntityLinks.linkToItemResource(part, Part.idExtractor);
		PartModel partModel = new PartModel();
		partModel.setPart(part);
		partModel.add(link);
		partModel.add(link.withSelfRel());
		return partModel;
	}
}

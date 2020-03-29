package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.kerneldc.flightlogserver.aircraftmaintenance.controller.ComponentController;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistoryModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Component
public class ComponentModelAssembler extends RepresentationModelAssemblerSupport<Component, ComponentModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public ComponentModelAssembler() {
		super(ComponentController.class, ComponentModel.class);
	}

	@Override
	public ComponentModel toModel(Component component) {
		LinkBuilder componentLinkBuilder = repositoryEntityLinks.linkForItemResource(component, Component.idExtractor);
		ComponentModel componentModel = new ComponentModel();
		componentModel.setComponent(component);
		
		componentModel.setPart(component.getPart());
		
		Set<ComponentHistoryModel> componentHistoryResourceSet = new LinkedHashSet<>();
		for (ComponentHistory componentHistory : component.getComponentHistorySet()) {
			ComponentHistoryModel componentHistoryModel = new ComponentHistoryModel();
			LinkBuilder componentHistorylinkBuilder = repositoryEntityLinks.linkForItemResource(componentHistory, ComponentHistory.idExtractor);
			componentHistoryModel.setComponentHistory(componentHistory);
			componentHistoryModel.setPart(componentHistory.getPart());
			componentHistoryModel.add(componentHistorylinkBuilder.withSelfRel());
			componentHistoryModel.add(componentHistorylinkBuilder.withRel(ComponentHistory.class.getSimpleName().toLowerCase()));
			componentHistoryResourceSet.add(componentHistoryModel);
		}
		componentModel.setComponentHistoryResourceSet(componentHistoryResourceSet);

		componentModel.add(componentLinkBuilder.withSelfRel());
		componentModel.add(componentLinkBuilder.withRel(Component.class.getSimpleName().toLowerCase()));
		componentModel.add(componentLinkBuilder.slash(Component.PROPERTY_PART).withRel(Component.PROPERTY_PART));
		componentModel.add(componentLinkBuilder.slash(Component.PROPERTY_COMPONENT_HISTORY_SET).withRel(Component.PROPERTY_COMPONENT_HISTORY_SET));
		return componentModel;
	}
}

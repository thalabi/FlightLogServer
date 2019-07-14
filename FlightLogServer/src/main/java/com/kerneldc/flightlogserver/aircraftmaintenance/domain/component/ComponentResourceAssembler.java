package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.kerneldc.flightlogserver.aircraftmaintenance.controller.ComponentController;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistoryResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Component
public class ComponentResourceAssembler extends ResourceAssemblerSupport<Component, ComponentResource> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public ComponentResourceAssembler() {
		super(ComponentController.class, ComponentResource.class);
	}

	@Override
	public ComponentResource toResource(Component component) {
		LinkBuilder componentLinkBuilder = repositoryEntityLinks.linkForSingleResource(component);
		ComponentResource componentResource = new ComponentResource();
		componentResource.setComponent(component);
		
		componentResource.setPart(component.getPart());
		
		Set<ComponentHistoryResource> componentHistoryResourceSet = new LinkedHashSet<>();
		for (ComponentHistory componentHistory : component.getComponentHistorySet()) {
			ComponentHistoryResource componentHistoryResource = new ComponentHistoryResource();
			LinkBuilder componentHistorylinkBuilder = repositoryEntityLinks.linkForSingleResource(componentHistory);
			componentHistoryResource.setComponentHistory(componentHistory);
			componentHistoryResource.setPart(componentHistory.getPart());
			componentHistoryResource.add(componentHistorylinkBuilder.withSelfRel());
			componentHistoryResource.add(componentHistorylinkBuilder.withRel(ComponentHistory.class.getSimpleName().toLowerCase()));
			componentHistoryResourceSet.add(componentHistoryResource);
		}
		componentResource.setComponentHistoryResourceSet(componentHistoryResourceSet);

		componentResource.add(componentLinkBuilder.withSelfRel());
		componentResource.add(componentLinkBuilder.withRel(Component.class.getSimpleName().toLowerCase()));
		componentResource.add(componentLinkBuilder.slash(Component.PROPERTY_PART).withRel(Component.PROPERTY_PART));
		componentResource.add(componentLinkBuilder.slash(Component.PROPERTY_COMPONENT_HISTORY_SET).withRel(Component.PROPERTY_COMPONENT_HISTORY_SET));
		return componentResource;
	}
	
	@Override
	public ComponentResource instantiateResource(Component component) {
		return new ComponentResource();
	}
}

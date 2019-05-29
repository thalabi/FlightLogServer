package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.kerneldc.flightlogserver.aircraftmaintenance.controller.ComponentController;

@org.springframework.stereotype.Component
public class ComponentResourceAssembler extends ResourceAssemblerSupport<Component, ComponentResource> {

	@Autowired
	private EntityLinks repositoryEntityLinks;
	
	public ComponentResourceAssembler() {
		super(ComponentController.class, ComponentResource.class);
	}

	@Override
	public ComponentResource toResource(Component component) {
		LinkBuilder linkBuilder = repositoryEntityLinks.linkForSingleResource(component);
		ComponentResource componentResource = new ComponentResource();
		componentResource.setComponent(component);
		componentResource.setPart(component.getPart());
		componentResource.setComponentHistorySet(component.getComponentHistorySet());
		componentResource.add(linkBuilder.withSelfRel());
		componentResource.add(linkBuilder.withRel(Component.class.getSimpleName().toLowerCase()));
		componentResource.add(linkBuilder.slash(Component.PROPERTY_PART).withRel(Component.PROPERTY_PART));
		componentResource.add(linkBuilder.slash(Component.PROPERTY_COMPONENT_HISTORY_SET).withRel(Component.PROPERTY_COMPONENT_HISTORY_SET));
		return componentResource;
	}
	
	@Override
	public ComponentResource instantiateResource(Component component) {
		return new ComponentResource();
	}
}

package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.server.LinkBuilder;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.IComplexEntityRepresentationModelAssembler;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistoryModel;
import com.kerneldc.flightlogserver.domain.AbstractEntity;
import com.kerneldc.flightlogserver.domain.AbstractEntityModel;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class ComponentModelAssembler implements IComplexEntityRepresentationModelAssembler {

	private final RepositoryEntityLinks repositoryEntityLinks;
	
	@Override
	public AbstractEntityModel toModel(AbstractEntity entity) {
		LinkBuilder componentLinkBuilder = repositoryEntityLinks.linkForItemResource(entity, AbstractEntity.idExtractor);
		Component component = (Component)entity;
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

	@Override
	public boolean canHandle(Class<? extends AbstractEntity> entityType) {
		return entityType.equals(Component.class);
	}

}

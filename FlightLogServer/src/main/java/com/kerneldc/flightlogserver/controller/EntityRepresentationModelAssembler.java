package com.kerneldc.flightlogserver.controller;

import java.util.Collection;

import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.IComplexEntityRepresentationModelAssembler;
import com.kerneldc.flightlogserver.domain.AbstractEntity;
import com.kerneldc.flightlogserver.domain.AbstractEntityModel;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Component
@Slf4j
public class EntityRepresentationModelAssembler extends RepresentationModelAssemblerSupport<AbstractEntity, AbstractEntityModel> {

	private final RepositoryEntityLinks repositoryEntityLinks;
	private final Collection<IComplexEntityRepresentationModelAssembler> iComplexEntityRepresentationModelAssemblers;
	
	public EntityRepresentationModelAssembler(RepositoryEntityLinks repositoryEntityLinks, Collection<IComplexEntityRepresentationModelAssembler> complexEntityRepresentationModelAssemblerss) {
		super(GenericEntityController.class, AbstractEntityModel.class);
		
		this.repositoryEntityLinks = repositoryEntityLinks;
		this.iComplexEntityRepresentationModelAssemblers = complexEntityRepresentationModelAssemblerss;
		
		LOGGER.info("Loading complex entities model assemblers:");
		for (IComplexEntityRepresentationModelAssembler iComplexEntityRepresentationModelAssembler: complexEntityRepresentationModelAssemblerss) {
			
			LOGGER.info("[{}]", iComplexEntityRepresentationModelAssembler.getClass().getSimpleName());
		}
	}

	@Override
	public AbstractEntityModel toModel(AbstractEntity entity) {
		
		// Handle a complex entity
		for (IComplexEntityRepresentationModelAssembler iComplexEntityRepresentationModelAssembler: iComplexEntityRepresentationModelAssemblers) {
			
			if (iComplexEntityRepresentationModelAssembler.canHandle(entity.getClass())) {
				LOGGER.info("handling complex entity: [{}]", entity.getClass().getSimpleName());
				return iComplexEntityRepresentationModelAssembler.toModel(entity);
			}
			
		}
		
		// Otherwise handle a simple entity
		LOGGER.info("handling simple entity: [{}]", entity.getClass().getSimpleName());
		AbstractEntityModel model = new AbstractEntityModel();
		model.setAbstractEntity(entity);
		Link link = repositoryEntityLinks.linkToItemResource(entity, AbstractEntity.idExtractor);
		model.add(link);
		model.add(link.withSelfRel());
		
		return model;
	}
//	@Override
//	public AbstractEntityModel toModel(AbstractEntity entity) {
//		
//		LOGGER.info("resource getTypeName: [{}]", getResourceType().getTypeName());
//		
//		AbstractEntityModel model = new AbstractEntityModel();
//		model.setAbstractEntity(entity);
//
//		
//		if (entity instanceof Airport airport) {
//			LOGGER.info("airport mame: [{}]", airport.getName());
//		}
//		if (entity instanceof Component component) {
//			LOGGER.info("component mame: [{}]", component.getName());
//			LOGGER.info("history size: [{}]", component.getComponentHistorySet().size());
//			for (IComplexEntityRepresentationModelAssembler complexEntityRepresentationModelAssembler: iComplexEntityRepresentationModelAssemblers) {
//				
//				if (complexEntityRepresentationModelAssembler.canHandle(entity.getClass())) {
//					LOGGER.info("handling component");
//					return complexEntityRepresentationModelAssembler.toModel(entity);
//				}
//				
//			}
//		}
//		if (entity instanceof FlightLogTotalsV flightLogtotalsV) {
//			LOGGER.info("component route from: [{}]", flightLogtotalsV.getRouteFrom());
//		}
//		
//		Link link = repositoryEntityLinks.linkToItemResource(entity, AbstractEntity.idExtractor);
//		model.add(link);
//		model.add(link.withSelfRel());
//
//		
//		
//		return model;
//	}

}

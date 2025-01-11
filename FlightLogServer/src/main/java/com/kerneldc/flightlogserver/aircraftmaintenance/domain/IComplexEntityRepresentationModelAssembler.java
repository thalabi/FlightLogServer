package com.kerneldc.flightlogserver.aircraftmaintenance.domain;

import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.kerneldc.flightlogserver.domain.AbstractEntity;
import com.kerneldc.flightlogserver.domain.AbstractEntityModel;

public interface IComplexEntityRepresentationModelAssembler extends RepresentationModelAssembler<AbstractEntity, AbstractEntityModel> {

	@Override
	AbstractEntityModel toModel(AbstractEntity entity);
	
	boolean canHandle(Class<? extends AbstractEntity> entityType);
}

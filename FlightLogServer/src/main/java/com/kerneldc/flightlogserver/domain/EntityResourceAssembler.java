package com.kerneldc.flightlogserver.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

//@Component
public class EntityResourceAssembler<E extends AbstractPersistableEntity, R extends ResourceSupport, C>
		extends ResourceAssemblerSupport<E, R> {

	private Class<E> classE;
	private Class<R> classR;
	
	@Autowired
	private EntityLinks repositoryEntityLinks;
	
	public EntityResourceAssembler(Class<E> classE, Class<R> classR, Class<C> classC) {
		super(classC, classR);
	}

	@Override
	public R toResource(E airport) {
//		Link link = repositoryEntityLinks.linkToSingleResource(airport);
//		R airportResource = classR.newInstance();
//		airportResource.setAirport(airport);
//		airportResource.add(link);
//		airportResource.add(link.withSelfRel());
//		return airportResource;
		return null;
	}
	
	@Override
	public R instantiateResource(E airport) {
		try {
			return classR.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}

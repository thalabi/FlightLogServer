package com.kerneldc.flightlogserver.security.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.security.controller.UserController;

@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

	@Autowired
	private EntityLinks repositoryEntityLinks;
	
	public UserResourceAssembler() {
		super(UserController.class, UserResource.class);
	}

	@Override
	public UserResource toResource(User user) {
		Link link = repositoryEntityLinks.linkToSingleResource(user);
		UserResource userResource = new UserResource();
		userResource.setUser(user);
		userResource.add(link);
		userResource.add(link.withSelfRel());
		return userResource;
	}
	
	@Override
	public UserResource instantiateResource(User user) {
		return new UserResource();
	}
}

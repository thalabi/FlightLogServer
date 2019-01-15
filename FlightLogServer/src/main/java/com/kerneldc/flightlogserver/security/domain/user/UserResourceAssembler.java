package com.kerneldc.flightlogserver.security.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.LinkBuilder;
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
		LinkBuilder linkBuilder = repositoryEntityLinks.linkForSingleResource(user);
		UserResource userResource = new UserResource();
		userResource.setUser(user);
		userResource.add(linkBuilder.withSelfRel());
		userResource.add(linkBuilder.slash(User.PROPERTY_GROUP_SET).withRel(User.PROPERTY_GROUP_SET));
		return userResource;
	}
	
	@Override
	public UserResource instantiateResource(User user) {
		return new UserResource();
	}
}

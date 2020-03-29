package com.kerneldc.flightlogserver.security.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.security.controller.UserController;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport <User, UserModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public UserModelAssembler() {
		super(UserController.class, UserModel.class);
	}

	@Override
	public UserModel toModel(User user) {
		LinkBuilder linkBuilder = repositoryEntityLinks.linkForItemResource(user, User.idExtractor);
		UserModel userModel = new UserModel();
		userModel.setUser(user);
		userModel.add(linkBuilder.withSelfRel());
		userModel.add(linkBuilder.slash(User.PROPERTY_GROUP_SET).withRel(User.PROPERTY_GROUP_SET));
		return userModel;
	}
}

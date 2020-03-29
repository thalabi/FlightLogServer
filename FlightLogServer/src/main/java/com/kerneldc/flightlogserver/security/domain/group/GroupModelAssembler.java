package com.kerneldc.flightlogserver.security.domain.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.security.controller.GroupController;

@Component
public class GroupModelAssembler extends RepresentationModelAssemblerSupport <Group, GroupModel> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public GroupModelAssembler() {
		super(GroupController.class, GroupModel.class);
	}

	@Override
	public GroupModel toModel(Group group) {
		LinkBuilder linkBuilder = repositoryEntityLinks.linkForItemResource(group, Group.idExtractor);
		GroupModel groupModel = new GroupModel();
		groupModel.setGroup(group);
		groupModel.add(linkBuilder.withSelfRel());
		groupModel.add(linkBuilder.slash(Group.PROPERTY_USER_SET).withRel(Group.PROPERTY_USER_SET));
		groupModel.add(linkBuilder.slash(Group.PROPERTY_PERMISSION_SET).withRel(Group.PROPERTY_PERMISSION_SET));
		return groupModel;
	}
}

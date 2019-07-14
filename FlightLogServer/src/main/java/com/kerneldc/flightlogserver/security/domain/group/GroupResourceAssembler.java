package com.kerneldc.flightlogserver.security.domain.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.security.controller.GroupController;

@Component
public class GroupResourceAssembler extends ResourceAssemblerSupport<Group, GroupResource> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;
	
	public GroupResourceAssembler() {
		super(GroupController.class, GroupResource.class);
	}

	@Override
	public GroupResource toResource(Group group) {
		LinkBuilder linkBuilder = repositoryEntityLinks.linkForSingleResource(group);
		GroupResource groupResource = new GroupResource();
		groupResource.setGroup(group);
		groupResource.add(linkBuilder.withSelfRel());
		groupResource.add(linkBuilder.slash(Group.PROPERTY_USER_SET).withRel(Group.PROPERTY_USER_SET));
		groupResource.add(linkBuilder.slash(Group.PROPERTY_PERMISSION_SET).withRel(Group.PROPERTY_PERMISSION_SET));
		return groupResource;
	}
	
	@Override
	public GroupResource instantiateResource(Group group) {
		return new GroupResource();
	}
}

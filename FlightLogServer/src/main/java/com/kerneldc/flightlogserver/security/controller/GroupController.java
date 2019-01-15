package com.kerneldc.flightlogserver.security.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.controller.ControllerHelper;
import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.security.domain.group.Group;
import com.kerneldc.flightlogserver.security.domain.group.GroupResource;
import com.kerneldc.flightlogserver.security.domain.group.GroupResourceAssembler;
import com.kerneldc.flightlogserver.security.repository.GroupRepository;

@RestController
@RequestMapping("groupController")
@ExposesResourceFor(Group.class) // needed for unit test to create entity links
public class GroupController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private GroupRepository groupRepository;
    
	private GroupResourceAssembler groupResourceAssembler;
	
    public GroupController(GroupRepository groupRepository, GroupResourceAssembler groupResourceAssembler) {
        this.groupRepository = groupRepository;
        this.groupResourceAssembler = groupResourceAssembler;
    }

    @GetMapping("/findAll")
	public PagedResources<GroupResource> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Group> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Group> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Group> groupPage = groupRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(GroupController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toResource(groupPage, groupResourceAssembler, link);
    }

}

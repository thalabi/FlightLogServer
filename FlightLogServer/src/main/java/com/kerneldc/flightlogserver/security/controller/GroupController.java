package com.kerneldc.flightlogserver.security.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.controller.ControllerHelper;
import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.security.domain.group.Group;
import com.kerneldc.flightlogserver.security.domain.group.GroupModel;
import com.kerneldc.flightlogserver.security.domain.group.GroupModelAssembler;
import com.kerneldc.flightlogserver.security.repository.GroupRepository;

@RestController
@RequestMapping("groupController")
//@ExposesResourceFor(Group.class) // needed for unit test to create entity links
public class GroupController {

	private GroupRepository groupRepository;
    
	private GroupModelAssembler groupModelAssembler;
	
    public GroupController(GroupRepository groupRepository, GroupModelAssembler groupModelAssembler) {
        this.groupRepository = groupRepository;
        this.groupModelAssembler = groupModelAssembler;
    }

    @GetMapping("/findAll")
	public PagedModel<GroupModel> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Group> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Group> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Group> groupPage = groupRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toModel(groupPage, groupModelAssembler, link);
    }

}

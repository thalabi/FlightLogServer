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
import com.kerneldc.flightlogserver.security.domain.user.User;
import com.kerneldc.flightlogserver.security.domain.user.UserResource;
import com.kerneldc.flightlogserver.security.domain.user.UserResourceAssembler;
import com.kerneldc.flightlogserver.security.repository.UserRepository;

@RestController
@RequestMapping("userController")
@ExposesResourceFor(User.class) // needed for unit test to create entity links
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private UserRepository userRepository;
    
	private UserResourceAssembler userResourceAssembler;
	
    public UserController(UserRepository userRepository, UserResourceAssembler userResourceAssembler) {
        this.userRepository = userRepository;
        this.userResourceAssembler = userResourceAssembler;
    }

    @GetMapping("/findAll")
	public PagedResources<UserResource> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<User> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<User> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<User> userPage = userRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(UserController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toResource(userPage, userResourceAssembler, link);
    }

}

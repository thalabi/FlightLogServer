package com.kerneldc.flightlogserver.controller;

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

import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.Registration;
import com.kerneldc.flightlogserver.domain.RegistrationResource;
import com.kerneldc.flightlogserver.domain.RegistrationResourceAssembler;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.repository.RegistrationRepository;

@RestController
@RequestMapping("registrationController")
@ExposesResourceFor(Registration.class) // needed for unit test to create entity links
public class RegistrationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private RegistrationRepository registrationRepository;
    
	private RegistrationResourceAssembler registrationResourceAssembler;
	
    public RegistrationController(RegistrationRepository registrationRepository, RegistrationResourceAssembler registrationResourceAssembler) {
        this.registrationRepository = registrationRepository;
        this.registrationResourceAssembler = registrationResourceAssembler;
    }

    @GetMapping("/findAll")
	public PagedResources<RegistrationResource> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Registration> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Registration> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Registration> registrationPage = registrationRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(RegistrationController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toResource(registrationPage, registrationResourceAssembler, link);
    }
}

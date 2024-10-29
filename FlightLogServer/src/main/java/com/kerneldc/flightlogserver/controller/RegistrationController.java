package com.kerneldc.flightlogserver.controller;

import java.util.List;
import java.util.Objects;

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

import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.domain.registration.Registration;
import com.kerneldc.flightlogserver.domain.registration.RegistrationModel;
import com.kerneldc.flightlogserver.domain.registration.RegistrationModelAssembler;
import com.kerneldc.flightlogserver.repository.RegistrationRepository;

@RestController
@RequestMapping("/protected/registrationController")
//@ExposesResourceFor(Registration.class) // needed for unit test to create entity links
public class RegistrationController {

    private RegistrationRepository registrationRepository;
    
	private RegistrationModelAssembler registrationModelAssembler;
	
    public RegistrationController(RegistrationRepository registrationRepository, RegistrationModelAssembler registrationModelAssembler) {
        this.registrationRepository = registrationRepository;
        this.registrationModelAssembler = registrationModelAssembler;
    }

    @GetMapping("/findAll")
	public PagedModel<RegistrationModel> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Registration> pagedResourcesAssembler) {
    	Objects.requireNonNull(pagedResourcesAssembler, "pagedResourcesAssembler cannot be null for this controller to work");
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Registration> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Registration> registrationPage = registrationRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RegistrationController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toModel(registrationPage, registrationModelAssembler, link);
    }
}

package com.kerneldc.flightlogserver.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEvent;
import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEventResource;
import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEventResourceAssembler;
import com.kerneldc.flightlogserver.repository.SignificantEventRepository;

@RestController
@RequestMapping("significantEventController")
//@ExposesResourceFor(SignificantEvent.class) // needed for unit test to create entity links
public class SignificantEventController {

    private SignificantEventRepository significantEventRepository;
    
	private SignificantEventResourceAssembler significantEventResourceAssembler;
	
    public SignificantEventController(SignificantEventRepository significantEventRepository, SignificantEventResourceAssembler significantEventResourceAssembler) {
        this.significantEventRepository = significantEventRepository;
        this.significantEventResourceAssembler = significantEventResourceAssembler;
    }

    @GetMapping("/findAll")
	public PagedResources<SignificantEventResource> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<SignificantEvent> pagedResourcesAssembler) {
    	Objects.requireNonNull(pagedResourcesAssembler, "pagedResourcesAssembler cannot be null for this controller to work");
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<SignificantEvent> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<SignificantEvent> significantEventPage = significantEventRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(SignificantEventController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toResource(significantEventPage, significantEventResourceAssembler, link);
    }
}

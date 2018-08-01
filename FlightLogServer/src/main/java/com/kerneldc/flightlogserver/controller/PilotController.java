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
import com.kerneldc.flightlogserver.domain.Pilot;
import com.kerneldc.flightlogserver.domain.PilotResource;
import com.kerneldc.flightlogserver.domain.PilotResourceAssembler;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.repository.PilotRepository;

@RestController
@RequestMapping("pilotController")
@ExposesResourceFor(Pilot.class) // needed for unit test to create entity links
public class PilotController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private PilotRepository pilotRepository;
    
	private PilotResourceAssembler pilotResourceAssembler;
	
    public PilotController(PilotRepository pilotRepository, PilotResourceAssembler pilotResourceAssembler) {
        this.pilotRepository = pilotRepository;
        this.pilotResourceAssembler = pilotResourceAssembler;
    }

    @GetMapping("/findAll")
	public PagedResources<PilotResource> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Pilot> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Pilot> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Pilot> pilotPage = pilotRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(PilotController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toResource(pilotPage, pilotResourceAssembler, link);
    }
}

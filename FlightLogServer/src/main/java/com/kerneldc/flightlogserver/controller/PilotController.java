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
import com.kerneldc.flightlogserver.domain.pilot.Pilot;
import com.kerneldc.flightlogserver.domain.pilot.PilotModel;
import com.kerneldc.flightlogserver.domain.pilot.PilotModelAssembler;
import com.kerneldc.flightlogserver.repository.PilotRepository;

@RestController
@RequestMapping("/protected/pilotController")
//@ExposesResourceFor(Pilot.class) // needed for unit test to create entity links
public class PilotController {

    private PilotRepository pilotRepository;
    
	private PilotModelAssembler pilotModelAssembler;
	
    public PilotController(PilotRepository pilotRepository, PilotModelAssembler pilotModelAssembler) {
        this.pilotRepository = pilotRepository;
        this.pilotModelAssembler = pilotModelAssembler;
    }

    @GetMapping("/findAll")
	public PagedModel<PilotModel> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Pilot> pagedResourcesAssembler) {
    	Objects.requireNonNull(pagedResourcesAssembler, "pagedResourcesAssembler cannot be null for this controller to work");
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Pilot> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Pilot> pilotPage = pilotRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PilotController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toModel(pilotPage, pilotModelAssembler, link);
    }
}

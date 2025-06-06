package com.kerneldc.flightlogserver.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.kerneldc.flightlogserver.domain.airport.Airport;
import com.kerneldc.flightlogserver.domain.airport.AirportModel;
import com.kerneldc.flightlogserver.domain.airport.AirportModelAssembler;
import com.kerneldc.flightlogserver.repository.AirportRepository;

@RestController
@RequestMapping("airportController")
//@ExposesResourceFor(Airport.class) // needed for unit test to create entity links
public class AirportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AirportRepository airportRepository;
    
	private AirportModelAssembler airportModelAssembler;
	
    public AirportController(AirportRepository airportRepository, AirportModelAssembler airportModelAssembler) {
        this.airportRepository = airportRepository;
        this.airportModelAssembler = airportModelAssembler;
    }

    @GetMapping("/findAll")
	public PagedModel<AirportModel> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Airport> pagedResourcesAssembler) {
    	Objects.requireNonNull(pagedResourcesAssembler, "pagedResourcesAssembler cannot be null for this controller to work");
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Airport> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Airport> airportPage = airportRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AirportController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toModel(airportPage, airportModelAssembler, link);
    }

    /*
    @GetMapping("/findAll2")
	public HttpEntity<PagedResources<AirportModel>> findAll2(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Airport> pagedResourcesAssembler) {
    	LOGGER.info("search: {}", search);
    	LOGGER.info("pageable: {}", pageable);
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Airport> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Airport> airportPage = airportRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(AirportController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		
		PagedResources<AirportModel> airportPagedResources =
				pagedResourcesAssembler.toResource(airportPage, airportModelAssembler, link);
		
		LOGGER.debug("airportPagedResources: {}", airportPagedResources);
		
		HttpEntity<PagedResources<AirportModel>> r = ResponseEntity.status(HttpStatus.OK).body(airportPagedResources);
		
		LOGGER.debug("r: {}", r);
		return r;
    }
    */
}

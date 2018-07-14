package com.kerneldc.flightlogserver.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.domain.Airport;
import com.kerneldc.flightlogserver.domain.AirportResource;
import com.kerneldc.flightlogserver.domain.AirportResourceAssembler;
import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.repository.AirportRepository;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("airportController")
@ExposesResourceFor(Airport.class) // needed for unit test to create entity links
public class AirportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AirportRepository airportRepository;
    
	private AirportResourceAssembler airportResourceAssembler;
	
    public AirportController(JpaContext jpaContext, AirportRepository airportRepository, AirportResourceAssembler airportResourceAssembler) {
        this.airportRepository = airportRepository;
        this.airportResourceAssembler = airportResourceAssembler;
    }

    @GetMapping("/count")
	public Count count() {
    	return new Count(airportRepository.count());
    }

    @GetMapping("/findAll")
	public HttpEntity<PagedResources<AirportResource>> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Airport> pagedResourcesAssembler) {
    	LOGGER.info("search: {}", search);
    	LOGGER.info("pageable: {}", pageable);
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Airport> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Airport> airportPage = airportRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(AirportController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		
		PagedResources<AirportResource> airportPagedResources =
				pagedResourcesAssembler.toResource(airportPage, airportResourceAssembler, link);
		
		LOGGER.debug("airportPagedResources: {}", airportPagedResources);
		
		HttpEntity<PagedResources<AirportResource>> r = ResponseEntity.status(HttpStatus.OK).body(airportPagedResources);
		
		LOGGER.debug("r: {}", r);
		return r;
    }
    
    @Getter @Setter
    class Count {
    	Long count;
    	Count(Long count) {
    		this.count = count;
    	}
    }
}

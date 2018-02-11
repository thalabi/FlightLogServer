package com.kerneldc.flightlogserver.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.domain.FlightLog;
import com.kerneldc.flightlogserver.domain.FlightLogResource;
import com.kerneldc.flightlogserver.domain.FlightLogResourceAssembler;
import com.kerneldc.flightlogserver.domain.FlightLogSpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.repository.FlightLogRepository;

@RestController
@RequestMapping("flightLogController")
public class FlightLogController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private FlightLogRepository flightLogRepository;
	private FlightLogResourceAssembler flightLogResourceAssembler;

    public FlightLogController(FlightLogRepository repository, FlightLogResourceAssembler flightLogResourceAssembler) {
        this.flightLogRepository = repository;
        this.flightLogResourceAssembler = flightLogResourceAssembler;
    }

    @GetMapping("/findAllBySpec")
    @CrossOrigin(origins = "http://localhost:4200")
    public Page<FlightLog> findAllBySpec(@RequestParam(value = "search") String search, Pageable pageable) {
    	if (search != null) LOGGER.info("search: {}", search);
    	if (pageable != null) LOGGER.info("pageable: {}", pageable);
    	List<SearchCriteria> searchCriteriaList = searchStringToSearchCriteriaList(search);
    	FlightLogSpecificationsBuilder flightLogSpecificationsBuilder = new FlightLogSpecificationsBuilder();
//        return flightLogRepository.findAll(flightLogSpecificationsBuilder.with(searchCriteriaList).build()).stream()
//                //.filter(this::isCool)
//                .collect(Collectors.toList());
        return flightLogRepository.findAll(flightLogSpecificationsBuilder.with(searchCriteriaList).build(), pageable);
    }
    
    @GetMapping("/findAllBySpec2")
    @CrossOrigin(origins = "http://localhost:4200")
	public PagedResources<FlightLogResource> findAllBySpec2(@RequestParam(value = "search") String search,
			Pageable pageable, PagedResourcesAssembler<FlightLog> pagedResourcesAssembler) {
    	if (pagedResourcesAssembler != null) LOGGER.info("pagedAssembler: {}", pagedResourcesAssembler);
    	if (search != null) LOGGER.info("search: {}", search);
    	if (pageable != null) LOGGER.info("pageable: {}", pageable);
    	List<SearchCriteria> searchCriteriaList = searchStringToSearchCriteriaList(search);
    	FlightLogSpecificationsBuilder flightLogSpecificationsBuilder = new FlightLogSpecificationsBuilder();
        Page<FlightLog> flightLogPage = flightLogRepository.findAll(flightLogSpecificationsBuilder.with(searchCriteriaList).build(), pageable);
        LOGGER.debug("flightLogPage.getSize(): {}", flightLogPage.getSize());
		Link link = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(FlightLogController.class)
				.findAllBySpec2(search, pageable, pagedResourcesAssembler)).withSelfRel();
        return pagedResourcesAssembler.toResource(flightLogPage, flightLogResourceAssembler, link);
    }
    
    private List<SearchCriteria> searchStringToSearchCriteriaList(String search) {
    	List<SearchCriteria> searchCriteriaList = new ArrayList<>();
    	Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|<=|>=)(.+?),");
    	Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
        	LOGGER.debug("matcher.group(1): {} matcher.group(2): {} matcher.group(3): {}", matcher.group(1), matcher.group(2), matcher.group(3));
        	searchCriteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
    	return searchCriteriaList;
    }
}

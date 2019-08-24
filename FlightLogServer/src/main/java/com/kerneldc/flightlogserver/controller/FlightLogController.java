package com.kerneldc.flightlogserver.controller;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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

import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLogResource;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLogResourceAssembler;
import com.kerneldc.flightlogserver.repository.FlightLogRepository;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("flightLogController")
//@Import(HateoasAwareSpringDataWebConfiguration.class)
//@ExposesResourceFor(FlightLog.class) // needed for unit test to create entity links
public class FlightLogController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private FlightLogRepository flightLogRepository;
    
	private FlightLogResourceAssembler flightLogResourceAssembler;
	
	private EntityManager flightLogEntityManager;

    public FlightLogController(FlightLogRepository flightLogRepository, FlightLogResourceAssembler flightLogResourceAssembler) {
        this.flightLogRepository = flightLogRepository;
        this.flightLogResourceAssembler = flightLogResourceAssembler;
    }

    @GetMapping("/count")
	public Count count() {
    	return new Count(flightLogRepository.count());
    }

    @GetMapping("/getLastXDaysSum")
    @Transactional
	public Count getLastXDaysSum() {
		Session session = flightLogEntityManager.unwrap(Session.class);
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement preparedStatement = connection.prepareStatement("set @day_dual_sum = 0.0");
				preparedStatement.execute();
			}
		});
		return new Count(flightLogRepository.count());
	}

    @GetMapping("/findAll")
	public HttpEntity<PagedResources<FlightLogResource>> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<FlightLog> pagedResourcesAssembler) {
    	Objects.requireNonNull(pagedResourcesAssembler, "pagedResourcesAssembler cannot be null for this controller to work");
    	LOGGER.info("search: {}", search);
    	LOGGER.info("pageable: {}", pageable);
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<FlightLog> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<FlightLog> flightLogPage = flightLogRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(FlightLogController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		
		PagedResources<FlightLogResource> flightLogPagedResources =
				pagedResourcesAssembler.toResource(flightLogPage, flightLogResourceAssembler, link);
		
		LOGGER.debug("flightLogPagedResources: {}", flightLogPagedResources);
		
		HttpEntity<PagedResources<FlightLogResource>> r = ResponseEntity.status(HttpStatus.OK).body(flightLogPagedResources);
		
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

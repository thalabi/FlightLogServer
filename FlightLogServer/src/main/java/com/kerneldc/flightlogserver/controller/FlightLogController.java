package com.kerneldc.flightlogserver.controller;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.domain.flightLog.FlightLogModelAssembler;
import com.kerneldc.flightlogserver.repository.FlightLogRepository;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/protected/flightLogController")
//@Import(HateoasAwareSpringDataWebConfiguration.class)
//@ExposesResourceFor(FlightLog.class) // needed for unit test to create entity links
public class FlightLogController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private FlightLogRepository flightLogRepository;
    
//	private FlightLogModelAssembler flightLogModelAssembler;
	
	private EntityManager flightLogEntityManager;

    public FlightLogController(FlightLogRepository flightLogRepository, FlightLogModelAssembler flightLogModelAssembler) {
        this.flightLogRepository = flightLogRepository;
//        this.flightLogModelAssembler = flightLogModelAssembler;
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

//    @GetMapping("/findAll")
//	public HttpEntity<PagedModel<FlightLogModel>> findAll(
//			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<FlightLog> pagedResourcesAssembler) {
//    	Objects.requireNonNull(pagedResourcesAssembler, "pagedResourcesAssembler cannot be null for this controller to work");
//    	LOGGER.info("search: {}", search);
//    	LOGGER.info("pageable: {}", pageable);
//    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
//    	EntitySpecificationsBuilder<FlightLog> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
//        Page<FlightLog> flightLogPage = flightLogRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
//		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FlightLogController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
//		PagedModel<FlightLogModel> flightLogPagedResources =
//				pagedResourcesAssembler.toModel(flightLogPage, flightLogModelAssembler, link);
//		
//		LOGGER.debug("flightLogPagedResources: {}", flightLogPagedResources);
//		
//		HttpEntity<PagedModel<FlightLogModel>> r = ResponseEntity.status(HttpStatus.OK).body(flightLogPagedResources);
//		
//		LOGGER.debug("r: {}", r);
//		return r;
//    }
    
    @Getter @Setter
    class Count {
    	Long count;
    	Count(Long count) {
    		this.count = count;
    	}
    }
}

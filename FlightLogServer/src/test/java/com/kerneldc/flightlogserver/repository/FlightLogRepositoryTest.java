package com.kerneldc.flightlogserver.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.kerneldc.flightlogserver.domain.EntitySpecification;
import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FlightLogRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
	
	private static final FlightLog FLIGHT_LOG1 = new FlightLog();
	private static final FlightLog FLIGHT_LOG2 = new FlightLog();
	{
		FLIGHT_LOG1.setRegistration("GYAX");
		FLIGHT_LOG2.setDaySolo(4.1f);
	}
	
	@Autowired
    private FlightLogRepository flightLogRepository;
	
	@Test
	public void testFindAll_Registration_Success() {
		FlightLog savedFlightLog = entityManager.persist(FLIGHT_LOG1);
		SearchCriteria searchCriteria = new SearchCriteria("registration", "=", "GYAX");
		EntitySpecificationsBuilder<FlightLog> flightLogSpecificationsBuilder = new EntitySpecificationsBuilder<>();
		Specification<FlightLog> spec = flightLogSpecificationsBuilder.with(Arrays.asList(searchCriteria)).build();
		List<FlightLog> results = flightLogRepository.findAll(spec);
		assertThat(results, hasSize(1));
		assertThat(results, hasItem(allOf(
				Matchers.<FlightLog>hasProperty("id", equalTo(savedFlightLog.getId())),
				Matchers.<FlightLog>hasProperty("registration", equalTo(savedFlightLog.getRegistration())))));
	}

	@Test
	public void testFindAll_DaySolo_Success() {
		FlightLog savedFlightLog = entityManager.persist(FLIGHT_LOG2);
		EntitySpecification<FlightLog> spec = new EntitySpecification<>(new SearchCriteria("daySolo", ">", "4"));
		List<FlightLog> results = flightLogRepository.findAll(Specification.where(spec));
		assertThat(results, hasSize(1));
		assertThat(results, hasItem(allOf(
				Matchers.<FlightLog>hasProperty("id", equalTo(savedFlightLog.getId())),
				Matchers.<FlightLog>hasProperty("daySolo", equalTo(savedFlightLog.getDaySolo())))));
	}
}

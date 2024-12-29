package com.kerneldc.flightlogserver.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import javax.persistence.metamodel.EntityType;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;
import com.kerneldc.flightlogserver.search.EntitySpecification;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FlightLogRepositoryTest extends AbstractBaseTest {

	@Autowired
    private TestEntityManager testEntityManager;
	
	private static final FlightLog FLIGHT_LOG1 = new FlightLog();
	private static final FlightLog FLIGHT_LOG2 = new FlightLog();
	private static final FlightLog FLIGHT_LOG3 = new FlightLog();
	{
		FLIGHT_LOG1.setRegistration("GYAX");
		
		FLIGHT_LOG2.setDaySolo(4.1f);
		
		FLIGHT_LOG3.setDaySolo(4.1f);
		FLIGHT_LOG3.setInstrumentNoIfrAppr(1);
	}
	
	@Autowired
    private FlightLogRepository flightLogRepository;
	
	@Test
	void testFindAll_Registration_Success() {
		FlightLog savedFlightLog = testEntityManager.persist(FLIGHT_LOG1);
		
		EntityType<FlightLog> entityMetamodel = testEntityManager.getEntityManager().getMetamodel().entity(FlightLog.class);
		Specification<FlightLog> entitySpecification = new EntitySpecification<FlightLog>(entityMetamodel, "registration|Equals|GYAX");

		List<FlightLog> results = flightLogRepository.findAll(entitySpecification);
		assertThat(results, hasSize(1));
		assertThat(results, hasItem(allOf(
				Matchers.<FlightLog>hasProperty("id", equalTo(savedFlightLog.getId())),
				Matchers.<FlightLog>hasProperty("registration", equalTo(savedFlightLog.getRegistration())))));
	}

	@Test
	void testFindAll_DaySolo_Success() {
		testEntityManager.persist(FLIGHT_LOG2);
		FlightLog savedFlightLog = testEntityManager.persist(FLIGHT_LOG3);
		
		EntityType<FlightLog> entityMetamodel = testEntityManager.getEntityManager().getMetamodel().entity(FlightLog.class);
		Specification<FlightLog> entitySpecification = new EntitySpecification<FlightLog>(entityMetamodel, "daySolo|GT|4,instrumentNoIfrAppr|equals|1");
		
		List<FlightLog> results = flightLogRepository.findAll(entitySpecification);
		assertThat(results, hasSize(1));
		assertThat(results, hasItem(allOf(
				Matchers.<FlightLog>hasProperty("id", equalTo(savedFlightLog.getId())),
				Matchers.<FlightLog>hasProperty("daySolo", equalTo(savedFlightLog.getDaySolo())))));
	}

}

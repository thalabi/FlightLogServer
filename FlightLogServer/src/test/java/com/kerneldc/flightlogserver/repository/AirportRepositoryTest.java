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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.domain.airport.Airport;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AirportRepositoryTest extends AbstractBaseTest {

	@Autowired
    private TestEntityManager entityManager;
	
	private static final Airport AIRPORT1 = new Airport();
	private static final Airport AIRPORT2 = new Airport();
	private static final Airport AIRPORT3 = new Airport();
	private static final Airport AIRPORT4 = new Airport();
	private static final Airport AIRPORT5 = new Airport();
	private static final Airport AIRPORT6 = new Airport();
	{
		AIRPORT1.setIdentifier("CYOO");
		AIRPORT1.setName("Oshawa Municipal Airport");
		AIRPORT1.setCity("Oshawa");
		AIRPORT1.setProvince("Ontario");
		AIRPORT1.setCountry("Canada");
		AIRPORT1.setLatitude(43.925570f);
		AIRPORT1.setLongitude(-78.896864f);
		AIRPORT1.setUpperWindsStationId("CYYZ");
		
		AIRPORT2.setIdentifier("CYPQ");
		AIRPORT2.setName("Peterborough Municipal Airport");
		AIRPORT2.setCity("Peterborough");
		AIRPORT2.setProvince("Ontario");
		AIRPORT2.setCountry("Canada");
		AIRPORT2.setLatitude(44.233843f);
		AIRPORT2.setLongitude(-78.359167f);
		AIRPORT2.setUpperWindsStationId("CYYZ");

		AIRPORT3.setIdentifier("CNF4");
		AIRPORT3.setName("Kawartha Lakes Municipal Airport");
		AIRPORT3.setCity("Lindsay");
		AIRPORT3.setProvince("Ontario");
		AIRPORT3.setCountry("Canada");
		AIRPORT3.setLatitude(44.364278f);
		AIRPORT3.setLongitude(-78.776173f);
		AIRPORT3.setUpperWindsStationId("CYYZ");

		AIRPORT4.setIdentifier("CNY3");
		AIRPORT4.setName("Collingwood Airport");
		AIRPORT4.setCity("Collingwood");
		AIRPORT4.setProvince("Ontario");
		AIRPORT4.setCountry("Canada");
		AIRPORT4.setLatitude(44.447829f);
		AIRPORT4.setLongitude(-80.160036f);
		AIRPORT4.setUpperWindsStationId("CYYZ");

		AIRPORT5.setIdentifier("CNF4");
		AIRPORT5.setName("Kawartha Lakes Municipal Airport");
		AIRPORT5.setCity("Lindsay");
		AIRPORT5.setProvince("Ontario");
		AIRPORT5.setCountry("Canada");
		AIRPORT5.setLatitude(44.364278f);
		AIRPORT5.setLongitude(-78.776173f);
		AIRPORT5.setUpperWindsStationId("CYYZ");

		AIRPORT6.setIdentifier("CNY3");
		AIRPORT6.setName("Collingwood Airport");
		AIRPORT6.setCity("Collingwood");
		AIRPORT6.setProvince("Ontario");
		AIRPORT6.setCountry("Canada");
		AIRPORT6.setLatitude(44.447829f);
		AIRPORT6.setLongitude(-80.160036f);
		AIRPORT6.setUpperWindsStationId("CYYZ");

	}
	
	@Autowired
    private AirportRepository airportRepository;
	
	@Test
	public void testFindAll_ByIdentifier_Success() {
		Airport savedAirport = entityManager.persist(AIRPORT1);
		SearchCriteria searchCriteria = new SearchCriteria("identifier", "=", "CYOO");
		EntitySpecificationsBuilder<Airport> airportSpecificationsBuilder = new EntitySpecificationsBuilder<>();
		Specification<Airport> spec = airportSpecificationsBuilder.with(Arrays.asList(searchCriteria)).build();
		List<Airport> results = airportRepository.findAll(spec);
		assertThat(results, hasSize(1));
		assertThat(results, hasItem(allOf(
				Matchers.<Airport>hasProperty("id", equalTo(savedAirport.getId())),
				Matchers.<Airport>hasProperty("identifier", equalTo(savedAirport.getIdentifier())))));
	}

	@Test
	public void testFindAll_ByName_Success() {
		Airport savedAirport = entityManager.persist(AIRPORT2);
		SearchCriteria searchCriteria = new SearchCriteria("name", "=", "Peterborough Municipal Airport");
		EntitySpecificationsBuilder<Airport> airportSpecificationsBuilder = new EntitySpecificationsBuilder<>();
		Specification<Airport> spec = airportSpecificationsBuilder.with(Arrays.asList(searchCriteria)).build();
		List<Airport> results = airportRepository.findAll(spec);
		assertThat(results, hasSize(1));
		assertThat(results, hasItem(allOf(
				Matchers.<Airport>hasProperty("id", equalTo(savedAirport.getId())),
				Matchers.<Airport>hasProperty("name", equalTo(savedAirport.getName())))));
	}
	
	@Test
	public void testFindAll_ByCountry_Success() {
		entityManager.persist(AIRPORT3);
		entityManager.persist(AIRPORT4);
		SearchCriteria searchCriteria = new SearchCriteria("country", "=", "Canada");
		EntitySpecificationsBuilder<Airport> airportSpecificationsBuilder = new EntitySpecificationsBuilder<>();
		Specification<Airport> spec = airportSpecificationsBuilder.with(Arrays.asList(searchCriteria)).build();
		List<Airport> results = airportRepository.findAll(spec);
		assertThat(results, hasSize(2));
	}

	@Test
	public void testFindAll_ByProvinceAndCountry_Success() {
		entityManager.persist(AIRPORT5);
		entityManager.persist(AIRPORT6);
		SearchCriteria searchCriteria1 = new SearchCriteria("province", "=", "Ontario");
		SearchCriteria searchCriteria2 = new SearchCriteria("country", "=", "Canada");
		EntitySpecificationsBuilder<Airport> airportSpecificationsBuilder = new EntitySpecificationsBuilder<>();
		Specification<Airport> spec = airportSpecificationsBuilder.with(Arrays.asList(searchCriteria1, searchCriteria2)).build();
		List<Airport> results = airportRepository.findAll(spec);
		assertThat(results, hasSize(2));
	}
}

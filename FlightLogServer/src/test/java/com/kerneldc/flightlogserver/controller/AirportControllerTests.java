package com.kerneldc.flightlogserver.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.FlightLogServerApplication;
import com.kerneldc.flightlogserver.domain.airport.Airport;
import com.kerneldc.flightlogserver.repository.AirportRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightLogServerApplication.class)

public class AirportControllerTests extends AbstractBaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String BASE_URI = "/airportController";
	
	private MockMvc mockMvc;
	
	@MockBean
	private AirportRepository airportRepository;

	@Autowired
    private WebApplicationContext webApplicationContext;

	private static final MediaType halAndJsonContentType = MediaTypes.HAL_JSON_UTF8;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testFindAll( ) throws Exception {
		Airport airport = new Airport();
		airport.setId(7l);
		airport.setVersion(0l);
		airport.setIdentifier("CYOO");
		airport.setName("Oshawa Executive Airport");
		List<Airport> returnList = new ArrayList<>();
		returnList.add(airport);
		LOGGER.debug("returnList.getSize(): {}", returnList.size());
		Page<Airport> returnPage = new PageImpl<>(returnList, PageRequest.of(0, 20), 1);
		//returnPage.setPage
		LOGGER.debug("returnPage.getTotalElements(): {}", returnPage.getTotalElements());
		LOGGER.debug("returnPage.getSize(): {}", returnPage.getSize());
		
		
		// TODO create a pass a AirportSpecification object to the findAll method
		
		Mockito.when(airportRepository.findAll(ArgumentMatchers.<Specification<Airport>>any(), ArgumentMatchers.<Pageable>any()))
			.thenReturn(returnPage);

		//Mockito.when(airportResourceAssembler.toResource(airport)

		
		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/findAll").param("search", "name=CYOO"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(halAndJsonContentType))
//        .andExpect(jsonPath("$._embedded.airports[0].routeFrom", is(airport.getRouteFrom())))
//        .andExpect(jsonPath("$._embedded.airports[0].routeTo", is(airport.getRouteTo())))
//        .andExpect(jsonPath("$._embedded.airports[0].pic", is(airport.getPic())))
//        .andExpect(jsonPath("$._embedded.airports[0].daySolo", closeTo(airport.getDaySolo(), 0.0)))
//        .andExpect(jsonPath("$._embedded.airports[0].tosLdgsDay", equalTo(airport.getTosLdgsDay())))
//        .andExpect(jsonPath("$._links.self.href", is("http://localhost/airportController/findAll?search=routeFrom%3DCYOO")))
        .andReturn()
        ;
		LOGGER.debug("mvcResult.getResponse().getContentAsString(): {}", mvcResult.getResponse().getContentAsString());
	}
}

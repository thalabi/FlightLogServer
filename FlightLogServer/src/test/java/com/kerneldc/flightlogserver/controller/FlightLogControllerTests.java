package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.FlightLogServerApplication;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;
import com.kerneldc.flightlogserver.repository.FlightLogRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightLogServerApplication.class)
//@WebMvcTest(FlightLogController.class)
//@AutoConfigureTestDatabase
//@AutoConfigureMockMvc
//@EnableSpringDataWebSupport
//@Import({FlightLogResourceAssembler.class})

public class FlightLogControllerTests extends AbstractBaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String BASE_URI = "/flightLogController";
	
	private MockMvc mockMvc;
	
	@MockBean
	private JpaContext jpaContext;

	@MockBean
	private FlightLogRepository flightLogRepository;

	@Autowired
    private WebApplicationContext webApplicationContext;

	private static final MediaType jsonContentType = MediaType.APPLICATION_JSON_UTF8;
	private static final MediaType halAndJsonContentType = MediaTypes.HAL_JSON_UTF8;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testCount( ) throws Exception {
		Mockito.when(flightLogRepository.count())
			.thenReturn(1l);
		
		mockMvc.perform(get(BASE_URI + "/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(jsonContentType))
        .andExpect(jsonPath("$.count", is(1)))
//        .andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + userName)))
//        .andExpect(jsonPath("$.description", is("A description")))
        ;
	}
	
	@Test
	public void testFindAll( ) throws Exception {
		FlightLog flightLog = new FlightLog();
		flightLog.setId(7l);
		flightLog.setVersion(0l);
		flightLog.setRouteFrom("CYOO");
		flightLog.setRouteTo("CYPQ");
		flightLog.setPic("Tarif Halabi");
		flightLog.setDaySolo(1f);
		flightLog.setTosLdgsDay(1);
		List<FlightLog> returnList = new ArrayList<>();
		returnList.add(flightLog);
		LOGGER.debug("returnList.getSize(): {}", returnList.size());
		Page<FlightLog> returnPage = new PageImpl<>(returnList, PageRequest.of(0, 20), 1);
		//returnPage.setPage
		LOGGER.debug("returnPage.getTotalElements(): {}", returnPage.getTotalElements());
		LOGGER.debug("returnPage.getSize(): {}", returnPage.getSize());
		
		
		// TODO create a pass a FlightLogSpecification object to the findAll method
		
		Mockito.when(flightLogRepository.findAll(ArgumentMatchers.<Specification<FlightLog>>any(), ArgumentMatchers.<Pageable>any()))
			.thenReturn(returnPage);

		//Mockito.when(flightLogResourceAssembler.toResource(FLIGHT_LOG)

		
		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/findAll").param("search", "routeFrom=CYOO"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(halAndJsonContentType))
        .andExpect(jsonPath("$._embedded.flightLogs[0].routeFrom", is(flightLog.getRouteFrom())))
        .andExpect(jsonPath("$._embedded.flightLogs[0].routeTo", is(flightLog.getRouteTo())))
        .andExpect(jsonPath("$._embedded.flightLogs[0].pic", is(flightLog.getPic())))
        .andExpect(jsonPath("$._embedded.flightLogs[0].daySolo", closeTo(flightLog.getDaySolo(), 0.0)))
        .andExpect(jsonPath("$._embedded.flightLogs[0].tosLdgsDay", equalTo(flightLog.getTosLdgsDay())))
        .andExpect(jsonPath("$._links.self.href", is("http://localhost/flightLogController/findAll?search=routeFrom%3DCYOO")))
        .andReturn()
        ;
		LOGGER.debug("mvcResult.getResponse().getContentAsString(): {}", mvcResult.getResponse().getContentAsString());
	}
}

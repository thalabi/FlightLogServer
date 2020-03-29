package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.FlightLogServerApplication;
import com.kerneldc.flightlogserver.domain.pilot.Pilot;
import com.kerneldc.flightlogserver.repository.PilotRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightLogServerApplication.class)

public class SimpleControllerTests extends AbstractBaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String BASE_URI = "/simpleController";
	
	private MockMvc mockMvc;
	
	@MockBean
	private PilotRepository pilotRepository;

	@Autowired
    private WebApplicationContext webApplicationContext;

	private static final Pilot PILOT1 = new Pilot();
	{
		PILOT1.setId(7l);
		PILOT1.setVersion(0l);
		PILOT1.setPilot("Tarif Halabi");
	}
	
	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testCount( ) throws Exception {
		
		Mockito.when(pilotRepository.findAll())
			.thenReturn(Arrays.asList(PILOT1));

		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/findAll"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.[0].pilot", equalTo(PILOT1.getPilot())))
			.andExpect(jsonPath("$.[0].id", equalTo(PILOT1.getId().intValue())))
			.andReturn();
		LOGGER.debug("mvcResult.getResponse(): {}", mvcResult.getResponse().getContentAsString());
	}
	
}

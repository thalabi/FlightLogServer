package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.kerneldc.flightlogserver.FlightLogServerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightLogServerApplication.class)

public class AppInfoControllerTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String BASE_URI = "/appInfoController";
	
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;

	@Value("${build.timestamp}")
	private String buildTimestamp;

	private MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(), MediaType.TEXT_PLAIN.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testGetBuildTimestamp( ) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/getBuildTimestamp"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(content().string(equalTo(buildTimestamp)))
			.andReturn();
		LOGGER.debug("mvcResult.getResponse(): {}", mvcResult.getResponse().getContentAsString());
	}
}

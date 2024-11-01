package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.domain.pilot.Pilot;
import com.kerneldc.flightlogserver.domain.pilot.PilotModelAssembler;
import com.kerneldc.flightlogserver.repository.PilotRepository;
import com.kerneldc.flightlogserver.springBootConfig.WebSecurityConfig;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SimpleController.class)
@Import(WebSecurityConfig.class)
@Slf4j
class SimpleControllerTest extends AbstractBaseTest {

	private static final String PILOT_READ = WebSecurityConfig.AUTHORITY_PREFIX + "pilot" + WebSecurityConfig.READ_TABLE_SUFFIX;

	private static final String BASE_URI = "/protected/simpleController";
	
	@Autowired
	private MockMvc mockMvc;
    @MockBean
    private JwtDecoder jwtDecoder;
	
	@MockBean
	private PilotRepository pilotRepository;
	@MockBean
	private PilotModelAssembler pilotModelAssembler;

	private static final Pilot PILOT1 = new Pilot();
	{
		PILOT1.setId(7l);
		PILOT1.setVersion(0l);
		PILOT1.setPilot("Tarif Halabi");
	}
	
	@Test
	@WithMockUser(authorities = PILOT_READ)
	void testCount( ) throws Exception {
		
		Mockito.when(pilotRepository.findAll())
			.thenReturn(Arrays.asList(PILOT1));

		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/findAll"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.[0].pilot", equalTo(PILOT1.getPilot())))
			.andExpect(jsonPath("$.[0].id", equalTo(PILOT1.getId().intValue())))
			.andDo(print())
			.andReturn();
		LOGGER.debug("mvcResult.getResponse(): {}", mvcResult.getResponse().getContentAsString());
	}
	
}

package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLogModelAssembler;
import com.kerneldc.flightlogserver.repository.FlightLogRepository;
import com.kerneldc.flightlogserver.springBootConfig.WebSecurityConfig;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FlightLogController.class)
@Import(WebSecurityConfig.class)
@Slf4j
class FlightLogControllerTests extends AbstractBaseTest {

	private static final String FLIGHT_LOG_READ = WebSecurityConfig.AUTHORITY_PREFIX + "flight_log" + WebSecurityConfig.READ_TABLE_SUFFIX;

	private static final String BASE_URI = "/protected/flightLogController";
	
	@Autowired
	private MockMvc mockMvc;
    @MockBean
    private JwtDecoder jwtDecoder;

	@MockBean
	private FlightLogRepository flightLogRepository;
	@SpyBean // Note we are Mockito spy bean
	private FlightLogModelAssembler flightLogModelAssembler;
	@MockBean
	private RepositoryEntityLinks repositoryEntityLinks;


	@Test
	@WithMockUser(authorities = FLIGHT_LOG_READ)
	void testCount( ) throws Exception {
		Mockito.when(flightLogRepository.count())
			.thenReturn(1l);
		
		mockMvc.perform(get(BASE_URI + "/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.count", is(1)))
        ;
	}
	
	@Disabled // method findAll removed from FlightLogController. GenereicEntityController is now used instead
	@Test
	@WithMockUser(authorities = FLIGHT_LOG_READ)
	void testFindAll( ) throws Exception {
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
		LOGGER.debug("returnPage: {}", returnPage.getContent().get(0));
		LOGGER.debug("returnPage.getTotalElements(): {}", returnPage.getTotalElements());
		LOGGER.debug("returnPage.getSize(): {}", returnPage.getSize());
		
		
		// TODO create a pass a FlightLogSpecification object to the findAll method
		
		Mockito.when(flightLogRepository.findAll(ArgumentMatchers.<Specification<FlightLog>>any(), ArgumentMatchers.<Pageable>any()))
			.thenReturn(returnPage);

		Mockito.when(repositoryEntityLinks.linkToItemResource(flightLog, FlightLog.idExtractor))
			.thenReturn(Link.of("http://mocked-link"));
		
		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/findAll").param("search", "routeFrom=CYOO").contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded.flightLogs[0].routeFrom", is(flightLog.getRouteFrom())))
        .andExpect(jsonPath("$._embedded.flightLogs[0].routeTo", is(flightLog.getRouteTo())))
        .andExpect(jsonPath("$._embedded.flightLogs[0].pic", is(flightLog.getPic())))
        .andExpect(jsonPath("$._embedded.flightLogs[0].daySolo", closeTo(flightLog.getDaySolo(), 0.0)))
        .andExpect(jsonPath("$._embedded.flightLogs[0].tosLdgsDay", equalTo(flightLog.getTosLdgsDay())))
        .andExpect(jsonPath("$._links.self.href", is("http://localhost"+BASE_URI+"/findAll?search=routeFrom%3DCYOO")))
        .andDo(print())
        .andReturn()
        ;
		LOGGER.debug("mvcResult.getResponse().getContentAsString(): {}", mvcResult.getResponse().getContentAsString());
	}
}

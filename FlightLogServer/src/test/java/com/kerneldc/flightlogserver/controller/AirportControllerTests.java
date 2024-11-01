package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.domain.airport.Airport;
import com.kerneldc.flightlogserver.domain.airport.AirportModelAssembler;
import com.kerneldc.flightlogserver.repository.AirportRepository;
import com.kerneldc.flightlogserver.springBootConfig.UnauthorizedHandler;
import com.kerneldc.flightlogserver.springBootConfig.WebSecurityConfig;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AirportController.class)
@Import(WebSecurityConfig.class)
@Slf4j
class AirportControllerTests extends AbstractBaseTest {

	private static final String AIRPORT_READ = WebSecurityConfig.AUTHORITY_PREFIX + "airport" + WebSecurityConfig.READ_TABLE_SUFFIX;
	private static final String BASE_URI = "/protected/airportController";
	
	@Autowired
	private MockMvc mockMvc;
    @MockBean
    private JwtDecoder jwtDecoder;

	@MockBean
	private AirportRepository airportRepository;
	@SpyBean // Note we are Mockito spy bean
	private AirportModelAssembler airportModelAssembler;
	@MockBean
	private RepositoryEntityLinks repositoryEntityLinks;

//	@MockBean
//	private CustomUserDetailsServiceOld customUserDetailsServiceOld;
	@MockBean
	private UnauthorizedHandler unauthorizedHandler;
//	@MockBean
//	private JwtTokenProviderOld jwtTokenProviderOld;

	@Test
	@WithMockUser(authorities = AIRPORT_READ)
	void testFindAll( ) throws Exception {
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

		Mockito.when(repositoryEntityLinks.linkToItemResource(airport, Airport.idExtractor))
			.thenReturn(Link.of("http://mocked-link"));

		
		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/findAll").param("search", "name=CYOO"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded.airports[0].identifier", is(airport.getIdentifier())))
        .andExpect(jsonPath("$._embedded.airports[0].name", is(airport.getName())))
        .andReturn()
        ;
		LOGGER.debug("mvcResult.getResponse().getContentAsString(): {}", mvcResult.getResponse().getContentAsString());
	}
}
